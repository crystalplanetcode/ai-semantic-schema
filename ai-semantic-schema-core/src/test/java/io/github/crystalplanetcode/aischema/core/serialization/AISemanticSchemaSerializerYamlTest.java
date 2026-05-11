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
 * Test class for {@link AISemanticSchemaSerializerYaml}.
 *
 * @author Marcin Nowicki
 */
class AISemanticSchemaSerializerYamlTest {

	@Test
	void serializeShouldProduceValidYamlWithEntityAndFieldData() throws Exception {
		AISemanticSchema schema = buildSampleSchema();

		String yaml = AISemanticSchemaSerializerYaml.serialize(schema);

		assertTrue(yaml.contains("orders"));
		assertTrue(yaml.contains("Customer Orders"));
		assertTrue(yaml.contains("total_amount"));
		assertTrue(yaml.contains("Total order value"));
		assertTrue(yaml.contains("id"));
		assertTrue(yaml.contains("Order identifier"));
	}

	@Test
	void serializeShouldIncludeMetricsAtTopLevel() throws Exception {
		AISemanticSchema schema = buildSampleSchema();

		String yaml = AISemanticSchemaSerializerYaml.serialize(schema);

		assertTrue(yaml.contains("metrics:"));
		assertTrue(yaml.contains("total_revenue"));
		assertTrue(yaml.contains("SUM"));
	}

	@Test
	void serializeShouldNotStartWithDocumentMarker() throws Exception {
		AISemanticSchema schema = buildSampleSchema();

		String yaml = AISemanticSchemaSerializerYaml.serialize(schema);

		assertFalse(yaml.startsWith("---"));
	}

	@Test
	void serializeShouldOmitEmptyCollectionsAndNullFields() throws Exception {
		AIEntitySchema entitySchema = new AIEntitySchema();
		entitySchema.setEntity("sample.MinimalEntity");
		entitySchema.setTable("minimal_entity");

		AISemanticSchema schema = new AISemanticSchema();
		schema.processExtractedSchemas(List.of(entitySchema));

		String yaml = AISemanticSchemaSerializerYaml.serialize(schema);

		assertFalse(yaml.contains("aliases:"));
		assertFalse(yaml.contains("examples:"));
		assertFalse(yaml.contains("description: null"));
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

		String yaml = AISemanticSchemaSerializerYaml.serialize(schema);

		assertFalse(yaml.contains("aggregatable:"));
		assertFalse(yaml.contains("filterable:"));
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

		String yaml = AISemanticSchemaSerializerYaml.serialize(schema);

		assertTrue(yaml.contains("aggregatable: true"));
		assertTrue(yaml.contains("filterable: true"));
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
