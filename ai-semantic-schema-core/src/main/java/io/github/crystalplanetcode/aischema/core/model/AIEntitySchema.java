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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents the schema and an AI semantics, including its name, table,
 * identifier, attributes, relationships, and metrics. This class is used to
 * enrich the AI Semantic Schema with human-readable metadata about the entity,
 * which can be utilized during prompt generation to provide better context and
 * understanding for the AI model.
 * 
 * @author Marcin Nowicki
 */
@JsonPropertyOrder({ "entity", "table", "identifier", "tableSemantic", "attributes", "relationships", "metrics" })
public class AIEntitySchema {

	/** The name of the entity. */
	private String entity;

	/** The database table associated with the entity. */
	private String table;

	/** The identifier of the entity. */
	private AIEntityIdentifier identifier = null;

	/** The semantic information of the table associated with the entity. */
	private AITableSemantic tableSemantic = null;

	/** The list of attributes of the entity. */
	private List<AIEntityAttr> attributes = null;

	/** The list of semantic information for the relationships of the entity. */
	@JsonIgnore
	private List<AIEntityRelationSemantic> relationSemantics = null;

	/** The list of join graphs for the relationships of the entity. */
	@JsonProperty("relationships")
	private List<AIEntityJoinGraph> joinGraphs = null;

	/** The list of metrics for the table associated with the entity. */
	@JsonProperty("metrics")

	/** The list of metrics for the table associated with the entity. */
	private List<AITableMetricSemantic> tableMetrics = null;

	/** Default constructor. */
	public AIEntitySchema() {
	}

	/**
	 * Returns the name of the entity.
	 * 
	 * @return the name of the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * Sets the name of the entity.
	 * 
	 * @param entity the name of the entity
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * Returns the database table associated with the entity.
	 * 
	 * @return the database table associated with the entity
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Sets the database table associated with the entity.
	 * 
	 * @param table the database table associated with the entity
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * Returns the semantic information of the table associated with the entity.
	 * 
	 * @return the semantic information of the table associated with the entity
	 */
	public AITableSemantic getTableSemantic() {
		if (this.tableSemantic == null)
			this.tableSemantic = new AITableSemantic();
		return this.tableSemantic;
	}

	/**
	 * Sets the semantic information of the table associated with the entity.
	 * 
	 * @param tableSemantic the semantic information of the table associated with
	 *                      the entity
	 */
	public void setTableSemantic(AITableSemantic tableSemantic) {
		this.tableSemantic = tableSemantic;
	}

	/**
	 * Returns the identifier of the entity.
	 * 
	 * @return the identifier of the entity
	 */
	public AIEntityIdentifier getIdentifier() {
		if (this.identifier == null)
			this.identifier = new AIEntityIdentifier();
		return this.identifier;
	}

	/**
	 * Sets the identifier of the entity.
	 * 
	 * @param identifier the identifier of the entity
	 */
	public void setIdentifier(AIEntityIdentifier identifier) {
		this.identifier = identifier;
	}

	/**
	 * Returns the list of attributes of the entity.
	 * 
	 * @return the list of attributes of the entity
	 */
	public List<AIEntityAttr> getAttributes() {
		if (this.attributes == null)
			this.attributes = new ArrayList<AIEntityAttr>();
		return this.attributes;
	}

	/**
	 * Sets the list of attributes of the entity.
	 * 
	 * @param attributes the list of attributes of the entity
	 */
	public void setAttributes(List<AIEntityAttr> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Returns the list of join graphs for the relationships of the entity.
	 * 
	 * @return the list of join graphs for the relationships of the entity
	 */
	public List<AIEntityJoinGraph> getJoinGraphs() {
		if (this.joinGraphs == null)
			this.joinGraphs = new ArrayList<AIEntityJoinGraph>();
		return this.joinGraphs;
	}

	/**
	 * Sets the list of join graphs for the relationships of the entity.
	 * 
	 * @param joinGraphs the list of join graphs for the relationships of the entity
	 */
	public void setJoinGraphs(List<AIEntityJoinGraph> joinGraphs) {
		this.joinGraphs = joinGraphs;
	}

	/**
	 * Returns the list of metrics for the table associated with the entity.
	 * 
	 * @return the list of metrics for the table associated with the entity
	 */
	public List<AITableMetricSemantic> getTableMetrics() {
		if (this.tableMetrics == null)
			this.tableMetrics = new ArrayList<AITableMetricSemantic>();
		return this.tableMetrics;
	}

	/**
	 * Sets the list of metrics for the table associated with the entity.
	 * 
	 * @param tableMetrics the list of metrics for the table associated with the
	 *                     entity
	 */
	public void setTableMetrics(List<AITableMetricSemantic> tableMetrics) {
		this.tableMetrics = tableMetrics;
	}

	/**
	 * Returns the list of semantic information for the relationships of the entity.
	 * 
	 * @return the list of semantic information for the relationships of the entity
	 */
	public List<AIEntityRelationSemantic> getRelationSemantics() {
		if (this.relationSemantics == null)
			this.relationSemantics = new ArrayList<AIEntityRelationSemantic>();
		return this.relationSemantics;
	}
}
