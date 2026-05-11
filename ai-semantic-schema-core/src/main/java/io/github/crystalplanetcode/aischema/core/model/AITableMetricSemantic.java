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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.github.crystalplanetcode.aischema.core.annotation.AIMetric;

/**
 * Represents the semantic information of a database table metric provided by
 * the {@link AIMetric} annotation, including its name, label, description,
 * aliases, aggregation, and field. This class is used to enrich the AI Semantic
 * Schema with human-readable metadata about the table metrics, which can be
 * utilized during prompt generation to provide better context and understanding
 * for the AI model.
 * 
 * @author Marcin Nowicki
 */
@JsonPropertyOrder({ "name", "label", "description", "aliases", "aggregation", "field" })
public class AITableMetricSemantic {

	/** The name of the metric. */
	private String name;

	/** The label of the metric. */
	private String label;

	/** The description of the metric. */
	private String description;

	/** The list of aliases for the metric. */
	private List<String> aliases = null;

	/** The aggregation function of the metric. */
	private String aggregation;

	/** The field associated with the metric. */
	private String field;

	/** The table associated with the metric. */
	@JsonIgnore
	private String table;

	/** Default constructor. */
	public AITableMetricSemantic() {
	}

	/**
	 * Constructor with all properties.
	 * 
	 * @param name        the name of the metric
	 * @param label       the label of the metric
	 * @param description the description of the metric
	 * @param aliases     the list of aliases for the metric
	 * @param aggregation the aggregation function of the metric
	 * @param field       the field associated with the metric
	 * @param table       the table associated with the metric
	 */
	public AITableMetricSemantic(String name, String label, String description,
			List<String> aliases, String aggregation, String field, String table) {
		this.name = name;
		this.label = label;
		this.description = description;
		this.aliases = aliases;
		this.aggregation = aggregation;
		this.field = field;
		this.table = table;
	}

	/**
	 * Returns the name of the metric.
	 * 
	 * @return the name of the metric
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the metric.
	 * 
	 * @param name the name of the metric
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the label of the metric.
	 * 
	 * @return the label of the metric
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label of the metric.
	 * 
	 * @param label the label of the metric
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Returns the description of the metric.
	 * 
	 * @return the description of the metric
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the metric.
	 * 
	 * @param description the description of the metric
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the list of aliases for the metric.
	 * 
	 * @return the list of aliases for the metric
	 */
	public List<String> getAliases() {
		if (this.aliases == null)
			this.aliases = new ArrayList<>();
		return this.aliases;
	}

	/**
	 * Sets the list of aliases for the metric.
	 * 
	 * @param aliases the list of aliases for the metric
	 */
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	/**
	 * Returns the aggregation function of the metric.
	 * 
	 * @return the aggregation function of the metric
	 */
	public String getAggregation() {
		return aggregation;
	}

	/**
	 * Sets the aggregation function of the metric.
	 * 
	 * @param aggregation the aggregation function of the metric
	 */
	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	/**
	 * Returns the field associated with the metric.
	 * 
	 * @return the field associated with the metric
	 */
	public String getField() {
		return field;
	}

	/**
	 * Sets the field associated with the metric.
	 * 
	 * @param field the field associated with the metric
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * Returns the table associated with the metric.
	 * 
	 * @return the table associated with the metric
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Sets the table associated with the metric.
	 * 
	 * @param table the table associated with the metric
	 */
	public void setTable(String table) {
		this.table = table;
	}
}
