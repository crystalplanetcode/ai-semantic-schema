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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.mapping.AttributeMapping;
import org.hibernate.metamodel.mapping.ForeignKeyDescriptor;
import org.hibernate.metamodel.mapping.PluralAttributeMapping;
import org.hibernate.metamodel.spi.MappingMetamodelImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.crystalplanetcode.aischema.core.annotation.AIEnum;
import io.github.crystalplanetcode.aischema.core.annotation.AIField;
import io.github.crystalplanetcode.aischema.core.annotation.AIIgnore;
import io.github.crystalplanetcode.aischema.core.annotation.AIMetric;
import io.github.crystalplanetcode.aischema.core.annotation.AIRelation;
import io.github.crystalplanetcode.aischema.core.annotation.AITable;
import io.github.crystalplanetcode.aischema.core.model.AIEntityAttr;
import io.github.crystalplanetcode.aischema.core.model.AIEntityIdentifier;
import io.github.crystalplanetcode.aischema.core.model.AIEntityJoinGraph;
import io.github.crystalplanetcode.aischema.core.model.AIEntityRelationSemantic;
import io.github.crystalplanetcode.aischema.core.model.AIEntitySchema;
import io.github.crystalplanetcode.aischema.core.model.AIFieldSemantic;
import io.github.crystalplanetcode.aischema.core.model.AITableMetricSemantic;
import io.github.crystalplanetcode.aischema.core.model.AITableSemantic;
import jakarta.persistence.EntityManagerFactory;

/**
 * Extracts the complete physical schema of a JPA entity using Hibernate's own
 * runtime mapping metadata as the single, authoritative source of truth.
 *
 * <p>
 * <b>Design principle:</b>
 * <ul>
 * <li>Every table name, column name, FK/PK column, join table, etc. comes
 * directly from {@link EntityPersister} / {@link CollectionPersister} —
 * the exact same objects Hibernate uses when it generates SQL itself.
 * No snake_case guessing, no @Column re-reading, no naming-strategy
 * re-implementation.
 * <li>The semantic layer (@{@link AITable}, @{@link AIField}) is extracted
 * via plain Java reflection completely separate from the structural metadata
 * and stored in semantic fields in the resulting {@link AIEntitySchema}.
 * </ul>
 *
 * <p>
 * The resulting Schema {@link AIEntitySchema} is designed to be passed as part
 * of a prompt to an AI model.
 * Because the schema is identical to what Hibernate uses when generating SQL,
 * the model cannot hallucinate wrong tables names, column names or join
 * conditions.
 * They will always match what the database actually contains.
 * The semantic annotations allow the LLM to understand the meaning of each
 * table and column in a human-readable way allowing it to generate more
 * accurate and relevant queries.
 * 
 * @author Marcin Nowicki
 */
public class HibernateSchemaAIExtractor {

	private static final Logger log = LoggerFactory.getLogger(HibernateSchemaAIExtractor.class);

	private static final String UNKNOWN_TYPE = "unknown";
	private static final String BASIC = "BASIC";
	private static final String EMBEDDED = "EMBEDDED";
	private static final String MANY_TO_ONE = "MANY_TO_ONE";
	private static final String ONE_TO_ONE = "ONE_TO_ONE";
	private static final String ONE_TO_MANY = "ONE_TO_MANY";
	private static final String MANY_TO_MANY = "MANY_TO_MANY";

	private final SessionFactoryImplementor sessionFactory;
	private final MappingMetamodelImplementor mappingMetamodel;

	/**
	 * Utility method to convert a String array to a List or return an empty list if
	 * the array is null.
	 * 
	 * @param arr the array to convert
	 * @return a list containing the elements of the array, or an empty list if the
	 *         array is null
	 */
	private static <T> List<T> listOrEmpty(T[] arr) {
		return arr != null ? List.of(arr) : List.of();
	}

	// ======================================================
	// CONSTRUCTOR
	// ======================================================

	/**
	 * Creates a new HibernateSchemaAIExtractor using the given
	 * EntityManagerFactory.
	 * Unwraps the EMF to access Hibernate's internal SessionFactory and
	 * MappingMetamodel
	 * 
	 * @param emf the EntityManagerFactory to extract the schema from
	 * @throws IllegalArgumentException if the EMF cannot be unwrapped to a
	 *                                  Hibernate SessionFactoryImplementor
	 */
	public HibernateSchemaAIExtractor(EntityManagerFactory emf) {
		// Spring wraps the EMF in a JDK proxy, so casting
		// emf.unwrap(SessionFactory.class)
		// to SessionFactoryImplementor fails at runtime. Unwrapping to the implementor
		// interface directly bypasses the proxy and reaches the real Hibernate object.
		this.sessionFactory = emf.unwrap(SessionFactoryImplementor.class);
		// getMappingMetamodel() returns Hibernate's own MappingMetamodelImplementor —
		// the same metamodel the ORM uses internally for query building
		this.mappingMetamodel = this.sessionFactory.getMappingMetamodel();
	}

	// ======================================================
	// EXTRACT ENTITY SCHEMA
	// ======================================================

	/**
	 * Produces an {@link AIEntitySchema} describing the full physical + semantic
	 * schema of {@code entityClass}.
	 * 
	 * <p>
	 * Physical model (table, columns, FK/PK, nullability) → 100% from Hibernate.
	 * <p>
	 * Semantic model (human-readable descriptions) →
	 * from e.g. @{@link AITable}, @{@link AIField}, @{@link AIMetric}.
	 * 
	 * @param entityClass                   the JPA entity class to extract the
	 *                                      schema from
	 * @param excludeEntityRelationalFields whether to exclude relational fields
	 *                                      (associations to other entities) from
	 *                                      the schema attributes level.
	 *                                      Regardless of this setting, the owning
	 *                                      side of relationships (MANY_TO_ONE,
	 *                                      ONE_TO_ONE, MANY_TO_MANY) will be
	 *                                      included as join graph edges on entity
	 *                                      level
	 * @return the extracted schema as an {@link AIEntitySchema}
	 * @throws IllegalArgumentException if the given class is not a managed JPA
	 */
	public AIEntitySchema extractEntitySchema(Class<?> entityClass, boolean excludeEntityRelationalFields) {
		AIEntitySchema entitySchema = new AIEntitySchema();
		EntityPersister persister = mappingMetamodel.findEntityDescriptor(entityClass);

		if (persister == null) {
			String errMsg = "Class " + entityClass.getName()
					+ " is not a managed JPA entity or is not mapped by Hibernate";
			log.error(errMsg);
			throw new IllegalArgumentException(errMsg);
		}

		// -------------------------------------------------------------------
		// ENTITY — physical name as Hibernate registered it
		// -------------------------------------------------------------------
		entitySchema.setEntity(persister.getEntityName());

		// -------------------------------------------------------------------
		// TABLE — exact physical name, no heuristics
		// -------------------------------------------------------------------
		entitySchema.setTable(persister.getTableName());

		// -------------------------------------------------------------------
		// SEMANTIC — from @AiTable only; Hibernate has no opinion here
		// -------------------------------------------------------------------
		AITable aiTable = entityClass.getAnnotation(AITable.class);
		entitySchema.setTableSemantic(aiTable != null ? new AITableSemantic(
				aiTable.value(),
				aiTable.description(),
				new ArrayList<String>(listOrEmpty(aiTable.aliases())),
				aiTable.granularity())
				: null);

		// -------------------------------------------------------------------
		// IDENTIFIER — Hibernate keeps this separate from regular properties
		// -------------------------------------------------------------------
		String identifierSemantic = "";
		AIField aiIdentityField = findAIAnnotation(entityClass, persister.getIdentifierPropertyName(), AIField.class);

		if (aiIdentityField != null) {

			if (aiIdentityField.value() != null && !aiIdentityField.value().isEmpty()) {
				identifierSemantic = aiIdentityField.value();
			}
			if (aiIdentityField.description() != null && !aiIdentityField.description().isEmpty()) {
				identifierSemantic = aiIdentityField.description();
			}
		}

		entitySchema.setIdentifier(
				new AIEntityIdentifier(persister.getIdentifierPropertyName(),
						new ArrayList<String>(listOrEmpty(persister.getIdentifierColumnNames())),
						identifierSemantic));

		// -------------------------------------------------------------------
		// ATTRIBUTES — all mapped properties (including @MappedSuperclass fields)
		// -------------------------------------------------------------------
		String[] propertyNames = persister.getPropertyNames();
		Type[] propertyTypes = persister.getPropertyTypes();
		boolean[] nullability = persister.getPropertyNullability();

		List<AIEntityAttr> entityAttrs = entitySchema.getAttributes();

		for (int i = 0; i < propertyNames.length; i++) {

			String propName = propertyNames[i];
			Type propType = propertyTypes[i];
			boolean nullable = nullability[i];

			// If the field is annotated with @AIIgnore (e.g. auditable fields)
			// anywhere in the class hierarchy, skip it entirely
			// and the AI model won't even know it exists.
			AIIgnore aiIgnore = findAIAnnotation(entityClass, propName, AIIgnore.class);

			if (aiIgnore != null) {
				continue;
			}

			// getPropertyColumnNames() returns the real column name(s) as Hibernate
			// resolved them — through naming strategy, @Column(name=...), etc.
			String[] columns = persister.getPropertyColumnNames(propName);

			// ---------------------------------------------------------------
			// CLASSIFY the attribute kind using Hibernate's own Type hierarchy
			// ---------------------------------------------------------------
			String kind;
			String targetEntity = null;
			String joinTable = null;
			String[] joinKeyColumns = null;
			String[] joinTargetColumns = null;
			Class<?> javaType;

			if (propType instanceof EntityType entityType) {

				// MANY_TO_ONE or ONE_TO_ONE — owning side, FK columns are in this table
				kind = entityType.isOneToOne() ? ONE_TO_ONE : MANY_TO_ONE;
				targetEntity = entityType.getAssociatedEntityName();
				javaType = resolveFieldType(entityClass, propName);

			} else if (propType instanceof CollectionType collType) {

				// ONE_TO_MANY or MANY_TO_MANY
				CollectionPersister cp = mappingMetamodel.findCollectionDescriptor(collType.getRole());

				// FK key columns from Hibernate's PluralAttributeMapping runtime model
				joinKeyColumns = extractKeyColumns(persister, propName);

				if (cp != null && cp.isManyToMany()) {
					kind = MANY_TO_MANY;
					// cp.getTableName() is the intermediate join table
					joinTable = cp.getTableName();
					if (cp.getElementPersister() != null) {
						targetEntity = cp.getElementPersister().getEntityName();
						// PK columns of the target entity (the other side of the join table)
						joinTargetColumns = cp.getElementPersister().getIdentifierColumnNames();
					}
				} else {
					kind = ONE_TO_MANY;
					// FK lives in the target (element) table — joinKeyColumns above
					// are the owner PK columns that the target FK references
					if (cp != null && cp.getElementPersister() != null) {
						targetEntity = cp.getElementPersister().getEntityName();
					}
				}
				javaType = resolveFieldType(entityClass, propName);

			} else if (propType instanceof ComponentType) {

				kind = EMBEDDED;
				javaType = resolveFieldType(entityClass, propName);

			} else {

				// BASIC scalar — String, Number, LocalDate, Enum, etc.
				kind = BASIC;
				// For basic types, Hibernate's getReturnedClass() is the Java type
				javaType = propType.getReturnedClass();
			}

			boolean isEnum = javaType != null && javaType.isEnum();
			List<String> enumValues = isEnum ? getEnumValues(javaType) : null;

			AIEnum.Entry[] enumEntryArr = null;
			Map<String, String> enumEntries = null;

			if (isEnum) {

				AIEnum aiEnum = findAIAnnotation(entityClass, propName, AIEnum.class);
				if (aiEnum != null)
					enumEntryArr = aiEnum.value();

				enumEntries = new LinkedHashMap<String, String>();

				if (enumEntryArr != null) {

					for (AIEnum.Entry entry : enumEntryArr) {
						enumEntries.put(entry.key(), entry.description());
					}
				}
			}

			if (!(BASIC.equals(kind) || EMBEDDED.equals(kind))) {
				AIRelation aiRelation = findAIAnnotation(entityClass, propName, AIRelation.class);
				// If @AIRelation is present, include the relation semantic for future use in
				// the join graph not on field but on entity level
				if (aiRelation != null)
					entitySchema.getRelationSemantics()
							.add(new AIEntityRelationSemantic(propName, kind, aiRelation.value()));

				// Skip relational fields if the flag is set; they will be represented as join
				// graph edges on entity level skipping attribute level
				if (excludeEntityRelationalFields)
					continue;
			}

			AIField aiField = findAIAnnotation(entityClass, propName, AIField.class);

			entityAttrs.add(new AIEntityAttr(propName,
					new ArrayList<String>(listOrEmpty(columns)),
					kind,
					javaType != null ? javaType.getSimpleName() : UNKNOWN_TYPE,
					nullable,
					targetEntity,
					isEnum,
					enumValues,
					joinTable,
					new ArrayList<String>(listOrEmpty(joinKeyColumns)),
					new ArrayList<String>(listOrEmpty(joinTargetColumns)),
					aiField != null ? new AIFieldSemantic(
							aiField.value(),
							aiField.description(),
							new ArrayList<String>(listOrEmpty(aiField.aliases())),
							new ArrayList<String>(listOrEmpty(aiField.examples())),
							aiField.aggregatable(),
							aiField.filterable()) : null,
					enumEntries));

			if (isEnum && aiField != null && !aiField.filterable()) {
				log.warn("Extracted enum attribute: {}.{} Should have filterable = true in AIField annotation!",
						entityClass.getSimpleName(), propName);
			}
		}

		// -------------------------------------------------------------------
		// JOIN GRAPH
		// -------------------------------------------------------------------
		List<HibernateJoinEdge> joinGraph = buildJoinGraph(entityClass);

		entitySchema.setJoinGraphs(joinGraph.stream()
				.map(j -> new AIEntityJoinGraph(
						j.fromEntity(), j.fromTable(), j.toEntity(), j.toTable(),
						j.type(), j.fieldName(),
						new ArrayList<String>(listOrEmpty(j.fkColumns())),
						new ArrayList<String>(listOrEmpty(j.pkColumns())),
						j.joinTable()))
				.toList());

		// -------------------------------------------------------------------
		// SEMANTIC METRICS — from @AIMetrics; Hibernate has no opinion here
		// -------------------------------------------------------------------
		AIMetric[] metrics = entityClass.getAnnotationsByType(AIMetric.class);
		if (metrics.length > 0) {
			entitySchema.setTableMetrics(
					List.of(metrics).stream()
							.map(m -> new AITableMetricSemantic(
									m.name(),
									m.label(),
									m.description(),
									new ArrayList<String>(listOrEmpty(m.aliases())),
									m.aggregation(),
									m.field(),
									entitySchema.getTable()))
							.toList());
		}

		return entitySchema;
	}

	// ======================================================
	// BUILD JOIN GRAPH
	// ======================================================

	/**
	 * Extracts relevant FK→PK relationships between this entity and other entities
	 * as a list of {@link HibernateJoinEdge}.
	 * Uses Hibernate's own runtime mapping metadata to find the exact join
	 * conditions that Hibernate actually knows about.
	 *
	 * <p>
	 * Only the <em>owning</em> side of each association is emitted
	 * (MANY_TO_ONE, ONE_TO_ONE, MANY_TO_MANY). Inverse sides (ONE_TO_MANY
	 * with {@code mappedBy}) are implicit and are already represented by the
	 * MANY_TO_ONE edge going in the opposite direction.
	 * 
	 * @param entityClass the entity class to extract the join graph for
	 * @return a list of join edges representing the relationships from this entity
	 */
	protected List<HibernateJoinEdge> buildJoinGraph(Class<?> entityClass) {

		List<HibernateJoinEdge> joins = new ArrayList<HibernateJoinEdge>();

		EntityPersister persister = mappingMetamodel.findEntityDescriptor(entityClass).getEntityPersister();

		String fromEntity = persister.getEntityName();
		String fromTable = persister.getTableName();
		String[] propNames = persister.getPropertyNames();
		Type[] propTypes = persister.getPropertyTypes();

		for (int i = 0; i < propNames.length; i++) {

			String propName = propNames[i];
			Type propType = propTypes[i];

			if (propType instanceof EntityType entityType) {

				// Owning side: FK columns are physically in fromTable
				String[] fkColumns = persister.getPropertyColumnNames(propName);
				String targetEntityName = entityType.getAssociatedEntityName();

				EntityPersister targetPersister = mappingMetamodel.findEntityDescriptor(targetEntityName);
				String toTable = targetPersister != null ? targetPersister.getTableName() : targetEntityName;
				String[] pkColumns = targetPersister != null
						? targetPersister.getIdentifierColumnNames()
						: new String[0];

				joins.add(new HibernateJoinEdge(
						fromEntity,
						fromTable,
						targetEntityName,
						toTable,
						entityType.isOneToOne() ? ONE_TO_ONE : MANY_TO_ONE,
						propName,
						fkColumns,
						pkColumns,
						null));

			} else if (propType instanceof CollectionType collType) {

				CollectionPersister cp = mappingMetamodel.findCollectionDescriptor(collType.getRole());

				if (cp != null && cp.isManyToMany()) {

					// MANY_TO_MANY: both FK sides are in the join table
					String toEntity = cp.getElementPersister() != null
							? cp.getElementPersister().getEntityName()
							: "?";
					String toTable = cp.getElementPersister() != null
							? cp.getElementPersister().getTableName()
							: "?";
					String[] pkColumns = cp.getElementPersister() != null
							? cp.getElementPersister().getIdentifierColumnNames()
							: new String[0];

					joins.add(new HibernateJoinEdge(
							fromEntity,
							fromTable,
							toEntity,
							toTable,
							MANY_TO_MANY,
							propName,
							extractKeyColumns(persister, propName),
							pkColumns,
							cp.getTableName()));
				}

				// ONE_TO_MANY with mappedBy: the FK lives in the target (element) table.
				// Already represented by the MANY_TO_ONE edge from the target entity.
			}
		}

		return joins;
	}

	// ======================================================
	// HELPERS
	// ======================================================

	/**
	 * Returns the enum constant names of the given Java enum type, e.g. ["SMALL",
	 * "MEDIUM", "LARGE"].
	 * Used to give the LLM a complete picture of the possible values of an enum
	 * field.
	 *
	 * @param type the Java enum type
	 * @return a list of enum constant names
	 */
	private List<String> getEnumValues(Class<?> type) {
		Object[] constants = type.getEnumConstants();
		List<String> values = new ArrayList<String>();
		for (Object c : constants) {
			values.add(c.toString());
		}

		return values;
	}

	/**
	 * Walks the class hierarchy to find AI annotation e.g. @{@link AIField} on a
	 * field that may be declared in a {@code @MappedSuperclass} supertype.
	 * 
	 * @param entityClass     the annotated entity class
	 * @param fieldName       the name of the field to find the annotation on
	 * @param annotationClass the annotation class to look for
	 * @return the annotation instance found or null
	 */
	private <T extends Annotation> T findAIAnnotation(Class<?> entityClass, String fieldName,
			Class<T> annotationClass) {
		Class<?> cls = entityClass;
		while (cls != null && cls != Object.class) {
			try {
				Field field = cls.getDeclaredField(fieldName);
				// Field found in this class — return annotation or null, stop searching
				return field.getAnnotation(annotationClass);
			} catch (NoSuchFieldException ignored) {
				cls = cls.getSuperclass();
			}
		}

		return null;
	}

	/**
	 * Walks the class hierarchy to resolve the Java type of a field that may
	 * be declared in a {@code @MappedSuperclass} supertype.
	 * 
	 * @param entityClass the annotated entity class
	 * @param fieldName   the name of the field to resolve
	 * @return the Java type of the field or null if not found
	 */
	private Class<?> resolveFieldType(Class<?> entityClass, String fieldName) {
		Class<?> cls = entityClass;
		while (cls != null && cls != Object.class) {
			try {
				return cls.getDeclaredField(fieldName).getType();
			} catch (NoSuchFieldException ignored) {
				cls = cls.getSuperclass();
			}
		}

		return null;
	}

	/**
	 * Extracts the FK column names linking this entity's PK into the collection or
	 * join table for the given plural attribute.
	 *
	 * <p>
	 * Uses Hibernate's own {@link PluralAttributeMapping} runtime model, so the
	 * result is identical to what Hibernate uses when generating SQL itself.
	 * If the descriptor is not available for the attribute kind, returns an empty
	 * array rather than throwing.
	 * 
	 * @param ownerPersister the entity persister of the owning entity
	 * @param propName       the name of the plural attribute
	 * @return an array of FK column names or empty array if not available
	 */
	private String[] extractKeyColumns(EntityPersister ownerPersister, String propName) {
		try {
			AttributeMapping attrMap = ownerPersister.findAttributeMapping(propName);
			if (attrMap instanceof PluralAttributeMapping pam) {
				ForeignKeyDescriptor fkd = pam.getKeyDescriptor();
				if (fkd != null) {
					List<String> cols = new ArrayList<String>();
					// getKeyPart() is the side of the FK that lives in the
					// collection/join table; forEachSelectable iterates its columns
					fkd.getKeyPart().forEachSelectable((idx, sel) -> cols.add(sel.getSelectionExpression()));
					return cols.toArray(new String[0]);
				}
			}
		} catch (Exception ignored) {
			// Hibernate may not expose FK details for every collection kind
		}

		return new String[0];
	}
}
