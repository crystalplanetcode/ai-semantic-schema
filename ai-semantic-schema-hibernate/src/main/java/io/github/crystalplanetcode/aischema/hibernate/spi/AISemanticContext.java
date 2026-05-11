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

package io.github.crystalplanetcode.aischema.hibernate.spi;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.crystalplanetcode.aischema.core.model.AIEntitySchema;
import io.github.crystalplanetcode.aischema.core.model.AISemanticSchema;
import io.github.crystalplanetcode.aischema.core.serialization.AISemanticSchemaSerializerJson;
import io.github.crystalplanetcode.aischema.core.serialization.AISemanticSchemaSerializerYaml;

/**
 * Represents the semantic context extracted from JPA entities, enhanced by AI
 * annotations. This class encapsulates the processed semantic schema, which is
 * derived from the raw entity schemas extracted from JPA. The context provides
 * methods to access the processed schemas in various ways (e.g. by entity name,
 * by table name) and to serialize the context to different formats:
 * (YAML, JSON).
 * 
 * @author Marcin Nowicki
 */
public final class AISemanticContext {

	/**
	 * The semantic schema encapsulating the processed entity schemas and AI
	 * metadata.
	 */
	private final AISemanticSchema semanticSchema = new AISemanticSchema();

	/**
	 * Creates a new {@link AISemanticContext} based on the provided list of
	 * processed entity schemas. The constructor processes the extracted schemas and
	 * populates the internal semantic schema representation.
	 * 
	 * @param schemas the list of extracted entity schemas to be processed further
	 *                and included in the context
	 */
	public AISemanticContext(List<AIEntitySchema> schemas) {
		this.semanticSchema.processExtractedSchemas(List.copyOf(schemas));
	}

	/**
	 * Returns the list of processed entity schemas included in this context.
	 * 
	 * @return the list of processed entity schemas
	 */
	public List<AIEntitySchema> getProcessedSchemas() {
		return semanticSchema.getSchemas();
	}

	/**
	 * Returns a map of processed entity schemas keyed by their entity names.
	 * 
	 * @return a map of processed entity schemas by entity name
	 */
	public Map<String, AIEntitySchema> getProcessedSchemasByEntityName() {
		Map<String, AIEntitySchema> byEntity = new LinkedHashMap<String, AIEntitySchema>();
		for (AIEntitySchema schema : semanticSchema.getSchemas()) {
			byEntity.put(schema.getEntity() != null ? schema.getEntity() : String.valueOf(byEntity.size()), schema);
		}

		return byEntity;
	}

	/**
	 * Returns a map of processed entity schemas keyed by their table names.
	 * 
	 * @return a map of processed entity schemas by table name
	 */
	public Map<String, AIEntitySchema> getProcessedSchemasByTableName() {
		Map<String, AIEntitySchema> byTable = new LinkedHashMap<String, AIEntitySchema>();
		for (AIEntitySchema schema : semanticSchema.getSchemas()) {
			byTable.put(schema.getTable() != null ? schema.getTable() : String.valueOf(byTable.size()), schema);
		}

		return byTable;
	}

	/**
	 * Serializes the semantic context to YAML format.
	 * 
	 * @return the YAML representation of the semantic context
	 */
	public String toYaml() {
		try {
			return AISemanticSchemaSerializerYaml.serialize(semanticSchema);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to serialize semantic context to YAML", e);
		}
	}

	/**
	 * Serializes the semantic context to JSON format.
	 * 
	 * @return the JSON representation of the semantic context
	 */
	public String toJson() {
		try {
			return AISemanticSchemaSerializerJson.serialize(semanticSchema);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to serialize semantic context to JSON", e);
		}
	}

	/**
	 * Returns the underlying semantic schema.
	 * 
	 * @return the semantic schema
	 */
	public AISemanticSchema getSemanticSchema() {
		return this.semanticSchema;
	}
}
