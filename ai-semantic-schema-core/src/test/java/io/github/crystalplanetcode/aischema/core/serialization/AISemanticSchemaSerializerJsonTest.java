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

package io.github.crystalplanetcode.aischema.core.serialization;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.crystalplanetcode.aischema.core.model.AIEntityAttr;
import io.github.crystalplanetcode.aischema.core.model.AIEntityIdentifier;
import io.github.crystalplanetcode.aischema.core.model.AIEntitySchema;
import io.github.crystalplanetcode.aischema.core.model.AIFieldSemantic;
import io.github.crystalplanetcode.aischema.core.model.AISemanticSchema;
import io.github.crystalplanetcode.aischema.core.model.AITableMetricSemantic;
import io.github.crystalplanetcode.aischema.core.model.AITableSemantic;

/**
 * Test class for {@link AISemanticSchemaSerializerJson}.
 *
 * @author Marcin Nowicki
 */
class AISemanticSchemaSerializerJsonTest {

	@Test
	void serializeShouldProduceValidJsonWithEntityAndFieldData() throws Exception {
		AISemanticSchema schema = buildSampleSchema();

		String json = AISemanticSchemaSerializerJson.serialize(schema);

		assertTrue(json.contains("\"orders\""));
		assertTrue(json.contains("\"Customer Orders\""));
		assertTrue(json.contains("\"total_amount\""));
		assertTrue(json.contains("\"Total order value\""));
		assertTrue(json.contains("\"id\""));
		assertTrue(json.contains("\"Order identifier\""));
	}

	@Test
	void serializeShouldIncludeMetricsAtTopLevel() throws Exception {
		AISemanticSchema schema = buildSampleSchema();

		String json = AISemanticSchemaSerializerJson.serialize(schema);

		assertTrue(json.contains("\"metrics\""));
		assertTrue(json.contains("\"total_revenue\""));
		assertTrue(json.contains("\"SUM\""));
	}

	@Test
	void serializeShouldOmitEmptyCollectionsAndNullFields() throws Exception {
		AIEntitySchema entitySchema = new AIEntitySchema();
		entitySchema.setEntity("sample.MinimalEntity");
		entitySchema.setTable("minimal_entity");

		AISemanticSchema schema = new AISemanticSchema();
		schema.processExtractedSchemas(List.of(entitySchema));

		String json = AISemanticSchemaSerializerJson.serialize(schema);

		assertFalse(json.contains("\"aliases\""));
		assertFalse(json.contains("\"examples\""));
		assertFalse(json.contains("\"description\" : null"));
	}

	@Test
	void serializeShouldOmitAggregatableAndFilterableWhenFalse() throws Exception {
		AIFieldSemantic semantic = new AIFieldSemantic("Status", "", List.of(), List.of(), false, false);
		AIEntityAttr attr = new AIEntityAttr("status", List.of("status_col"), "BASIC", "String",
				true, null, false, null, null, List.of(), List.of(), semantic, null);

		AIEntitySchema entitySchema = new AIEntitySchema();
		entitySchema.setEntity("sample.OrderEntity");
		entitySchema.setTable("orders");
		entitySchema.setAttributes(List.of(attr));

		AISemanticSchema schema = new AISemanticSchema();
		schema.processExtractedSchemas(List.of(entitySchema));

		String json = AISemanticSchemaSerializerJson.serialize(schema);

		assertFalse(json.contains("\"aggregatable\""));
		assertFalse(json.contains("\"filterable\""));
	}

	@Test
	void serializeShouldIncludeAggregatableAndFilterableWhenTrue() throws Exception {
		AIFieldSemantic semantic = new AIFieldSemantic("Amount", "", List.of(), List.of(), true, true);
		AIEntityAttr attr = new AIEntityAttr("amount", List.of("amount_col"), "BASIC", "BigDecimal",
				false, null, false, null, null, List.of(), List.of(), semantic, null);

		AIEntitySchema entitySchema = new AIEntitySchema();
		entitySchema.setEntity("sample.OrderEntity");
		entitySchema.setTable("orders");
		entitySchema.setAttributes(List.of(attr));

		AISemanticSchema schema = new AISemanticSchema();
		schema.processExtractedSchemas(List.of(entitySchema));

		String json = AISemanticSchemaSerializerJson.serialize(schema);

		assertTrue(json.contains("\"aggregatable\" : true"));
		assertTrue(json.contains("\"filterable\" : true"));
	}

	private static AISemanticSchema buildSampleSchema() {
		AIEntityIdentifier identifier = new AIEntityIdentifier("id", List.of("id"), "Order identifier");

		AIFieldSemantic fieldSemantic = new AIFieldSemantic("Total order value",
				"Sum of all items in the order", List.of(), List.of(), true, false);
		AIEntityAttr attr = new AIEntityAttr("total_amount", List.of("total_amount"), "BASIC",
				"BigDecimal", false, null, false, null, null, List.of(), List.of(), fieldSemantic, null);

		AITableMetricSemantic metric = new AITableMetricSemantic(
				"total_revenue", "Total Revenue", "Sum of all order amounts",
				List.of(), "SUM", "total_amount", "orders");

		AIEntitySchema entitySchema = new AIEntitySchema();
		entitySchema.setEntity("sample.Order");
		entitySchema.setTable("orders");
		entitySchema.setIdentifier(identifier);
		entitySchema.setTableSemantic(new AITableSemantic("Customer Orders", "", List.of(), null));
		entitySchema.setAttributes(List.of(attr));
		entitySchema.setTableMetrics(List.of(metric));

		AISemanticSchema schema = new AISemanticSchema();
		schema.processExtractedSchemas(List.of(entitySchema));
		return schema;
	}
}
