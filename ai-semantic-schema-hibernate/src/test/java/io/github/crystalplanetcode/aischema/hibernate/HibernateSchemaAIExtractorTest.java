/*
 * Copyright 2026-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.crystalplanetcode.aischema.hibernate;

import jakarta.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.MappingMetamodelImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.CollectionType;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;

import io.github.crystalplanetcode.aischema.core.annotation.AIField;
import io.github.crystalplanetcode.aischema.core.annotation.AIIgnore;
import io.github.crystalplanetcode.aischema.core.annotation.AIMetric;
import io.github.crystalplanetcode.aischema.core.annotation.AITable;
import io.github.crystalplanetcode.aischema.core.model.AIEntitySchema;

/**
 * Test class for {@link HibernateSchemaAIExtractor}.
 * 
 * @author Marcin Nowicki
 */
class HibernateSchemaAIExtractorTest {

	@Test
	void extractEntitySchemaShouldFailForUnmanagedClass() {
		MappingMetamodelImplementor metamodel = mock(MappingMetamodelImplementor.class);
		HibernateSchemaAIExtractor extractor = newExtractor(metamodel);
		when(metamodel.findEntityDescriptor(UnmanagedEntity.class)).thenReturn(null);

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> extractor.extractEntitySchema(UnmanagedEntity.class, false));

		assertTrue(ex.getMessage().contains(UnmanagedEntity.class.getName()));
	}

	@Test
	void extractEntitySchemaShouldPopulateBasicMetadataAndIgnoreAnnotatedFields() {
		MappingMetamodelImplementor metamodel = mock(MappingMetamodelImplementor.class);
		EntityPersister persister = mock(EntityPersister.class);
		Type basicType = mock(Type.class);

		when(metamodel.findEntityDescriptor(SampleEntity.class)).thenReturn(persister);
		when(persister.getEntityPersister()).thenReturn(persister);
		when(persister.getEntityName()).thenReturn("sample.SampleEntity");
		when(persister.getTableName()).thenReturn("sample_entity");
		when(persister.getIdentifierPropertyName()).thenReturn("id");
		when(persister.getIdentifierColumnNames()).thenReturn(new String[] { "id_col" });
		when(persister.getPropertyNames()).thenReturn(new String[] { "name", "secret" });
		when(persister.getPropertyTypes()).thenReturn(new Type[] { basicType, basicType });
		when(persister.getPropertyNullability()).thenReturn(new boolean[] { false, true });
		when(persister.getPropertyColumnNames("name")).thenReturn(new String[] { "name_col" });
		when(persister.getPropertyColumnNames("secret")).thenReturn(new String[] { "secret_col" });
		doReturn(String.class).when(basicType).getReturnedClass();

		HibernateSchemaAIExtractor extractor = newExtractor(metamodel);
		AIEntitySchema schema = extractor.extractEntitySchema(SampleEntity.class, false);

		assertEquals("sample.SampleEntity", schema.getEntity());
		assertEquals("sample_entity", schema.getTable());
		assertEquals("Customer Orders", schema.getTableSemantic().getValue());
		assertEquals("Order identifier", schema.getIdentifier().getIdentifierSemantic());
		assertEquals(1, schema.getAttributes().size());
		assertEquals("name", schema.getAttributes().get(0).getName());
		assertEquals("BASIC", schema.getAttributes().get(0).getKind());
		assertEquals("Customer name", schema.getAttributes().get(0).getFieldSemantic().getValue());
		assertEquals(1, schema.getTableMetrics().size());
		assertEquals("total_orders", schema.getTableMetrics().get(0).getName());
	}

	@Test
	void extractEntitySchemaShouldKeepJoinGraphAndSkipManyToOneAttributeWhenExcluded() {
		MappingMetamodelImplementor metamodel = mock(MappingMetamodelImplementor.class);
		EntityPersister ownerPersister = mock(EntityPersister.class);
		EntityPersister targetPersister = mock(EntityPersister.class);
		EntityType relationType = mock(EntityType.class);

		when(metamodel.findEntityDescriptor(ManyToOneEntity.class)).thenReturn(ownerPersister);
		when(metamodel.findEntityDescriptor("sample.Customer")).thenReturn(targetPersister);
		when(ownerPersister.getEntityPersister()).thenReturn(ownerPersister);
		when(ownerPersister.getEntityName()).thenReturn("sample.ManyToOneEntity");
		when(ownerPersister.getTableName()).thenReturn("orders");
		when(ownerPersister.getIdentifierPropertyName()).thenReturn("id");
		when(ownerPersister.getIdentifierColumnNames()).thenReturn(new String[] { "id" });
		when(ownerPersister.getPropertyNames()).thenReturn(new String[] { "customer" });
		when(ownerPersister.getPropertyTypes()).thenReturn(new Type[] { relationType });
		when(ownerPersister.getPropertyNullability()).thenReturn(new boolean[] { false });
		when(ownerPersister.getPropertyColumnNames("customer")).thenReturn(new String[] { "customer_id" });

		when(relationType.isOneToOne()).thenReturn(false);
		when(relationType.getAssociatedEntityName()).thenReturn("sample.Customer");

		when(targetPersister.getTableName()).thenReturn("customers");
		when(targetPersister.getIdentifierColumnNames()).thenReturn(new String[] { "id" });

		HibernateSchemaAIExtractor extractor = newExtractor(metamodel);
		AIEntitySchema schema = extractor.extractEntitySchema(ManyToOneEntity.class, true);

		assertTrue(schema.getAttributes().isEmpty());
		assertEquals(1, schema.getJoinGraphs().size());
		assertEquals("MANY_TO_ONE", schema.getJoinGraphs().get(0).getType());
		assertEquals("customer", schema.getJoinGraphs().get(0).getFieldName());
		assertEquals("customer_id", schema.getJoinGraphs().get(0).getFkColumns().get(0));
		assertEquals("id", schema.getJoinGraphs().get(0).getPkColumns().get(0));
	}

	@Test
	void extractEntitySchemaShouldDetectManyToManyRelationAndJoinTable() {
		MappingMetamodelImplementor metamodel = mock(MappingMetamodelImplementor.class);
		EntityPersister ownerPersister = mock(EntityPersister.class);
		EntityPersister tagPersister = mock(EntityPersister.class);
		CollectionPersister collectionPersister = mock(CollectionPersister.class);
		CollectionType collectionType = mock(CollectionType.class);

		when(metamodel.findEntityDescriptor(ManyToManyEntity.class)).thenReturn(ownerPersister);
		when(metamodel.findCollectionDescriptor("sample.ManyToManyEntity.tags")).thenReturn(collectionPersister);
		when(ownerPersister.getEntityPersister()).thenReturn(ownerPersister);
		when(ownerPersister.getEntityName()).thenReturn("sample.ManyToManyEntity");
		when(ownerPersister.getTableName()).thenReturn("orders");
		when(ownerPersister.getIdentifierPropertyName()).thenReturn("id");
		when(ownerPersister.getIdentifierColumnNames()).thenReturn(new String[] { "id" });
		when(ownerPersister.getPropertyNames()).thenReturn(new String[] { "tags" });
		when(ownerPersister.getPropertyTypes()).thenReturn(new Type[] { collectionType });
		when(ownerPersister.getPropertyNullability()).thenReturn(new boolean[] { false });
		when(ownerPersister.getPropertyColumnNames("tags")).thenReturn(new String[0]);

		when(collectionType.getRole()).thenReturn("sample.ManyToManyEntity.tags");
		when(collectionPersister.isManyToMany()).thenReturn(true);
		when(collectionPersister.getTableName()).thenReturn("order_tags");
		when(collectionPersister.getElementPersister()).thenReturn(tagPersister);

		when(tagPersister.getEntityName()).thenReturn("sample.Tag");
		when(tagPersister.getTableName()).thenReturn("tags");
		when(tagPersister.getIdentifierColumnNames()).thenReturn(new String[] { "id" });

		HibernateSchemaAIExtractor extractor = newExtractor(metamodel);
		AIEntitySchema schema = extractor.extractEntitySchema(ManyToManyEntity.class, false);

		assertEquals(1, schema.getAttributes().size());
		assertEquals("MANY_TO_MANY", schema.getAttributes().get(0).getKind());
		assertEquals("order_tags", schema.getAttributes().get(0).getJoinTable());
		assertEquals("sample.Tag", schema.getAttributes().get(0).getTargetEntity());

		assertEquals(1, schema.getJoinGraphs().size());
		assertEquals("MANY_TO_MANY", schema.getJoinGraphs().get(0).getType());
		assertEquals("order_tags", schema.getJoinGraphs().get(0).getJoinTable());
	}

	private HibernateSchemaAIExtractor newExtractor(MappingMetamodelImplementor metamodel) {
		EntityManagerFactory emf = mock(EntityManagerFactory.class);
		SessionFactoryImplementor sessionFactory = mock(SessionFactoryImplementor.class);

		when(emf.unwrap(SessionFactoryImplementor.class)).thenReturn(sessionFactory);
		when(sessionFactory.getMappingMetamodel()).thenReturn(metamodel);

		return new HibernateSchemaAIExtractor(emf);
	}

	private static final class UnmanagedEntity {
	}

	@AITable("Customer Orders")
	@AIMetric(name = "total_orders", label = "Total orders", aggregation = "COUNT", field = "id")
	private static final class SampleEntity {

		@AIField(value = "Id", description = "Order identifier")
		private Long id;

		@AIField("Customer name")
		private String name;

		@AIIgnore
		private String secret;
	}

	@SuppressWarnings("unused")
	private static final class ManyToOneEntity {
		private Long id;
		private Customer customer;
	}

	@SuppressWarnings("unused")
	private static final class Customer {
		private Long id;
	}

	@SuppressWarnings("unused")
	private static final class ManyToManyEntity {
		private Long id;
		private java.util.List<Tag> tags;
	}

	@SuppressWarnings("unused")
	private static final class Tag {
		private Long id;
	}
}
