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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.github.crystalplanetcode.aischema.core.annotation.AITable;

/**
 * Represents the semantic information of a database table provided by
 * the {@link AITable} annotation, including its label, description, aliases,
 * and granularity. This class is used to enrich the AI Semantic Schema with
 * human-readable metadata about the tables, which can be
 * utilized during prompt generation to provide better context and understanding
 * for the AI model.
 * 
 * @author Marcin Nowicki
 */
@JsonPropertyOrder({ "label", "description", "aliases", "granularity" })
public class AITableSemantic {

	/** The label of the table. */
	@JsonProperty("label")
	private String value;

	/** The description of the table. */
	@JsonProperty("description")
	private String description;

	/** The list of aliases for the table. */
	private List<String> aliases = null;

	/** The granularity of the table. */
	private String granularity;

	/** Default constructor. */
	public AITableSemantic() {
	}

	/**
	 * Constructor with all properties.
	 * 
	 * @param value       the label of the table
	 * @param description the description of the table
	 * @param aliases     the list of aliases for the table
	 * @param granularity the granularity of the table
	 */
	public AITableSemantic(String value, String description, List<String> aliases, String granularity) {
		this.value = value;
		this.description = description;
		this.aliases = aliases;
		this.granularity = granularity;
	}

	/**
	 * Returns the label of the table.
	 * 
	 * @return the label of the table
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the label of the table.
	 * 
	 * @param value the label of the table
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns the description of the table.
	 * 
	 * @return the description of the table
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the table.
	 * 
	 * @param description the description of the table
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the list of aliases for the table.
	 * 
	 * @return the list of aliases for the table
	 */
	public List<String> getAliases() {
		if (this.aliases == null)
			this.aliases = new ArrayList<>();
		return this.aliases;
	}

	/**
	 * Sets the list of aliases for the table.
	 * 
	 * @param aliases the list of aliases for the table
	 */
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	/**
	 * Returns the granularity of the table.
	 * 
	 * @return the granularity of the table
	 */
	public String getGranularity() {
		return granularity;
	}

	/**
	 * Sets the granularity of the table.
	 * 
	 * @param granularity the granularity of the table
	 */
	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}
}
