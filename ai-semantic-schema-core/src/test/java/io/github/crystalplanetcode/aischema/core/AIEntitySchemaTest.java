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

package io.github.crystalplanetcode.aischema.core;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.crystalplanetcode.aischema.core.model.AIEntityAttr;
import io.github.crystalplanetcode.aischema.core.model.AIEntityJoinGraph;
import io.github.crystalplanetcode.aischema.core.model.AIEntitySchema;
import io.github.crystalplanetcode.aischema.core.model.AIFieldSemantic;
import io.github.crystalplanetcode.aischema.core.model.AISemanticSchema;
import io.github.crystalplanetcode.aischema.core.model.AITableMetricSemantic;
import io.github.crystalplanetcode.aischema.core.model.AITableSemantic;

/**
 * Test class for {@link AIEntitySchema}.
 * 
 * @author Marcin Nowicki
 */
class AIEntitySchemaTest {

	@Test
	void entitySchemaShouldLazyInitializeLists() {
		AIEntitySchema schema = new AIEntitySchema();

		assertNotNull(schema.getAttributes());
		assertTrue(schema.getAttributes().isEmpty());

		assertNotNull(schema.getJoinGraphs());
		assertTrue(schema.getJoinGraphs().isEmpty());

		assertNotNull(schema.getRelationSemantics());
		assertTrue(schema.getRelationSemantics().isEmpty());

		assertNotNull(schema.getTableMetrics());
		assertTrue(schema.getTableMetrics().isEmpty());
	}

	@Test
	void entityAttrShouldStoreBasicAndSemanticMetadata() {
		AIFieldSemantic semantic = new AIFieldSemantic("Name", "Customer name", List.of("full_name"), List.of("John"),
				true, true);
		AIEntityAttr attr = new AIEntityAttr("name", List.of("name_col"), "BASIC", "String", false, null, false, null,
				null, List.of(), List.of(), semantic, null);

		assertEquals("name", attr.getName());
		assertEquals("name_col", attr.getColumns().get(0));
		assertEquals("BASIC", attr.getKind());
		assertEquals("String", attr.getJavaType());
		assertFalse(attr.getNullable());
		assertFalse(attr.getIsEnum());
		assertEquals("Name", attr.getFieldSemantic().getValue());
		assertTrue(attr.getFieldSemantic().isAggregatable());
		assertTrue(attr.getFieldSemantic().isFilterable());
	}

	@Test
	void tableSemanticshouldMapPropertiesCorrectly() {
		AITableSemantic semantic = new AITableSemantic("Orders", "Customer orders table", List.of("Order", "Purchase"),
				"Each row is one order");

		assertEquals("Orders", semantic.getValue());
		assertEquals("Customer orders table", semantic.getDescription());
		assertEquals(2, semantic.getAliases().size());
		assertEquals("Each row is one order", semantic.getGranularity());
	}

	@Test
	void fieldSemanticShouldHandleAggregateAndFilterFlags() {
		AIFieldSemantic semantic = new AIFieldSemantic("Amount", "Order total", List.of(), List.of("100.00"), true,
				false);

		assertEquals("Amount", semantic.getValue());
		assertTrue(semantic.isAggregatable());
		assertFalse(semantic.isFilterable());
	}

	@Test
	void semanticSchemaShouldProcessExtractedSchemasAndOrganizeMetrics() {
		AIEntitySchema schema1 = new AIEntitySchema();
		schema1.setEntity("Order");
		schema1.setTable("orders");

		AITableMetricSemantic metric1 = new AITableMetricSemantic("total_orders", "Total Orders", "Sum of all orders",
				List.of(), "SUM", "amount", "orders");
		schema1.setTableMetrics(List.of(metric1));

		AIEntitySchema schema2 = new AIEntitySchema();
		schema2.setEntity("Customer");
		schema2.setTable("customers");
		schema2.setTableMetrics(List.of());

		AISemanticSchema semanticSchema = new AISemanticSchema();
		semanticSchema.processExtractedSchemas(List.of(schema1, schema2));

		assertEquals(2, semanticSchema.getSchemas().size());
		assertEquals("Order", semanticSchema.getSchemas().get(0).getEntity());

		assertNotNull(semanticSchema.getMetrics());
		assertEquals(1, semanticSchema.getMetrics().get("orders").size());
		assertEquals("total_orders", semanticSchema.getMetrics().get("orders").get(0).getName());

		assertTrue(semanticSchema.getMetrics().get("customers").isEmpty());
		assertNotNull(schema1.getTableMetrics());
		assertTrue(schema1.getTableMetrics().isEmpty());
	}

	@Test
	void semanticSchemaShouldEnrichJoinGraphsWithRelationSemantics() {
		AIEntitySchema schema = new AIEntitySchema();
		schema.setEntity("Order");
		schema.setTable("orders");

		AIEntityJoinGraph joinGraph = new AIEntityJoinGraph("Order", "orders", "Customer", "customers", "MANY_TO_ONE",
				"customer", List.of("customer_id"), List.of("id"), null);
		schema.setJoinGraphs(List.of(joinGraph));

		schema.getRelationSemantics().add(
				new io.github.crystalplanetcode.aischema.core.model.AIEntityRelationSemantic("customer", "MANY_TO_ONE",
						"Customer has many orders"));

		AISemanticSchema semanticSchema = new AISemanticSchema();
		semanticSchema.processExtractedSchemas(List.of(schema));

		assertEquals("Customer has many orders", schema.getJoinGraphs().get(0).getRelationSemantic());
	}
}
