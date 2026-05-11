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
package io.github.crystalplanetcode.aischema.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The AISemanticSchema class represents the overall semantic schema for AI
 * entities, including their attributes, relationships, and metrics. It serves
 * as a container for all the extracted semantic information from annotated Java
 * classes and provides methods to process and organize this information for use
 * in prompt generation for LLMs.
 * <p>
 * The class contains a list of {@link AIEntitySchema} objects,
 * each representing an individual entity with its attributes,
 * relationships, and metrics. It also contains a map of metrics organized by
 * table name for easier access during prompt generation.
 * 
 * @author Marcin Nowicki
 */
@JsonPropertyOrder({ "schemas", "metrics" })
public class AISemanticSchema {

	/**
	 * The list of {@link AIEntitySchema} objects representing the entities with
	 * semantic information extracted from annotated Java classes.
	 * This list is populated during the processing of extracted schemas and
	 * contains all the semantic information about the entities, including their
	 * attributes, relationships, and metrics. It is used as the main source of
	 * truth for prompt generation for LLMs.
	 * <p>
	 * The list is initialized as null and is reorganized by the
	 * processExtractedSchemas method, which takes a list of extracted
	 * {@link AIEntitySchema} objects and organizes them into the enriched semantic
	 * schema.
	 */
	private List<AIEntitySchema> schemas = null;

	/**
	 * The map of {@link AITableMetricSemantic} objects organized by table name.
	 * This map is populated during the processing of extracted schemas and contains
	 * all the metrics associated with each table. It is used to provide easy access
	 * to metrics during prompt generation for LLMs, allowing to include relevant
	 * metrics in the generated prompts based on the tables involved in the query.
	 * <p>
	 * The map is initialized as null and is populated by the
	 * processExtractedSchemas method, which takes a list of extracted
	 * {@link AIEntitySchema} objects and organizes
	 * their metrics into the map based on their associated table names.
	 */
	private Map<String, List<AITableMetricSemantic>> metrics = null;

	/** Default constructor */
	public AISemanticSchema() {
	}

	/**
	 * Processes the extracted schemas by organizing metrics by table and enriching
	 * join graphs with relation semantics hints. This method should be called after
	 * all schemas have been extracted and before using the semantic schema for
	 * prompt generation.
	 * 
	 * @param schemas the list of extracted {@link AIEntitySchema}
	 *                objects to be processed
	 */
	public void processExtractedSchemas(List<AIEntitySchema> schemas) {
		this.schemas = schemas;
		this.metrics = new HashMap<String, List<AITableMetricSemantic>>();

		// Extract metrics from entity schemas and put them level up to the semantic
		// schema for clarity and better readability for LLMs.
		schemas.stream()
				.forEach(s -> this.metrics.put(
						s.getTable(),
						s.getTableMetrics() != null ? s.getTableMetrics() : List.of()));

		// Clear metrics from entity schemas to avoid redundancy and potential confusion
		// during prompt generation, as metrics are now organized on top level in the
		// metrics mapped by table name.
		schemas.forEach(s -> s.setTableMetrics(null));

		// Enrich join graphs with relation semantics hints
		// Relation semantics are copied from {@link AIEntityRelationSemantic} list to
		// join graphs based on matching field and relation type.
		// {@link AIEntityRelationSemantic} list is created during schema extraction
		// from annotation {@link AIRelation} and contains relational semantic
		// hints. This allows to generate more accurate and semantically rich prompts
		// for LLMs.
		schemas.forEach(s -> s.getJoinGraphs().stream()
				.forEach(j -> j.setRelationSemantic(
						s.getRelationSemantics().stream()
								.filter(r -> r.field().equals(j.getFieldName())
										&& r.kind().equals(j.getType()))
								.findFirst()
								.map(AIEntityRelationSemantic::hint)
								.orElse(""))));
	}

	/**
	 * Returns the list of processed {@link AIEntitySchema} objects representing the
	 * entities
	 * 
	 * @return the list of processed {@link AIEntitySchema} objects
	 */
	public List<AIEntitySchema> getSchemas() {
		return this.schemas;
	}

	/**
	 * Returns the map of processed {@link AITableMetricSemantic} objects organized
	 * by table name.
	 * 
	 * @return the map of processed {@link AITableMetricSemantic} objects
	 *         organized by table name
	 */
	public Map<String, List<AITableMetricSemantic>> getMetrics() {
		return this.metrics;
	}

}
