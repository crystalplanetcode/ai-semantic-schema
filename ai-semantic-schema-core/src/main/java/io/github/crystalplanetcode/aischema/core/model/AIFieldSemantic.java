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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.github.crystalplanetcode.aischema.core.annotation.AIField;

/**
 * Represents the semantic information of a field {@link AIField} annotation,
 * including its label, description, aliases, examples, and flags for whether
 * the field is aggregatable or filterable. This class is used to enrich the AI
 * Semantic Schema with human-readable metadata about the fields, which can be
 * utilized during prompt generation to provide better context and understanding
 * for the AI model.
 * 
 * @author Marcin Nowicki
 */
@JsonPropertyOrder({ "label", "description", "aliases", "examples", "aggregatable" })
public class AIFieldSemantic {

	/** The label of the field. */
	@JsonProperty("label")
	private String value;

	/** The description of the field. */
	@JsonProperty("description")
	private String description;

	/** The list of aliases for the field. */
	private List<String> aliases = null;

	/** The list of examples for the field. */
	private List<String> examples = null;

	/** Flag indicating whether the field is aggregatable. */
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private boolean aggregatable;

	/** Flag indicating whether the field is filterable. */
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private boolean filterable;

	/** Default constructor. */
	public AIFieldSemantic() {
	}

	/**
	 * Constructor with all properties.
	 * 
	 * @param value        the label of the field
	 * @param description  the description of the field
	 * @param aliases      the list of aliases for the field
	 * @param examples     the list of examples for the field
	 * @param aggregatable flag indicating whether the field is aggregatable
	 * @param filterable   flag indicating whether the field is filterable
	 */
	public AIFieldSemantic(String value, String description, List<String> aliases, List<String> examples,
			boolean aggregatable, boolean filterable) {
		this.value = value;
		this.description = description;
		this.aliases = aliases;
		this.examples = examples;
		this.aggregatable = aggregatable;
		this.filterable = filterable;
	}

	/**
	 * Returns the label of the field.
	 * 
	 * @return the label of the field
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the label of the field.
	 * 
	 * @param value the label of the field
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns the description of the field.
	 * 
	 * @return the description of the field
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the field.
	 * 
	 * @param description the description of the field
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the list of aliases for the field.
	 * 
	 * @return the list of aliases for the field
	 */
	public List<String> getAliases() {
		if (this.aliases == null)
			this.aliases = new ArrayList<>();
		return this.aliases;
	}

	/**
	 * Sets the list of aliases for the field.
	 * 
	 * @param aliases the list of aliases for the field
	 */
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	/**
	 * Returns the list of examples for the field.
	 * 
	 * @return the list of examples for the field
	 */
	public List<String> getExamples() {
		if (this.examples == null)
			this.examples = new ArrayList<>();
		return this.examples;
	}

	/**
	 * Sets the list of examples for the field.
	 * 
	 * @param examples the list of examples for the field
	 */
	public void setExamples(List<String> examples) {
		this.examples = examples;
	}

	/**
	 * Returns whether the field is aggregatable.
	 * 
	 * @return true if the field is aggregatable, false otherwise
	 */
	public boolean isAggregatable() {
		return aggregatable;
	}

	/**
	 * Sets whether the field is aggregatable.
	 * 
	 * @param aggregatable true if the field is aggregatable, false otherwise
	 */
	public void setAggregatable(boolean aggregatable) {
		this.aggregatable = aggregatable;
	}

	/**
	 * Returns whether the field is filterable.
	 * 
	 * @return true if the field is filterable, false otherwise
	 */
	public boolean isFilterable() {
		return filterable;
	}

	/**
	 * Sets whether the field is filterable.
	 * 
	 * @param filterable true if the field is filterable, false otherwise
	 */
	public void setFilterable(boolean filterable) {
		this.filterable = filterable;
	}
}
