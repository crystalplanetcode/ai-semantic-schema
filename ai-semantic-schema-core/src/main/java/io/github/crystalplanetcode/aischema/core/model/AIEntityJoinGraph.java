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

import io.github.crystalplanetcode.aischema.core.annotation.AIRelation;

/**
 * Represents the join graph information for an AI entity, including the source
 * and target entities and tables, the type of relationship, the foreign key and
 * primary key columns, and any join table information. This class is used to
 * enrich the AI Semantic Schema with human-readable metadata about the
 * relationships between entities, which can be utilized during prompt
 * generation to provide better context and understanding for the AI model.
 * 
 * @author Marcin Nowicki
 */
@JsonPropertyOrder({ "fromTable", "toTable", "type", "fkColumns", "pkColumns", "joinTable" })
public class AIEntityJoinGraph {

	/** The source entity of the relationship. */
	@JsonIgnore
	private String fromEntity;

	/** The source table of the relationship. */
	private String fromTable;

	/** The target entity of the relationship. */
	@JsonIgnore
	private String toEntity;

	/** The target table of the relationship. */
	private String toTable;

	/** The type of relationship (e.g., "one-to-many", "many-to-one"). */
	private String type;

	/** The field name of the relationship. */
	@JsonIgnore
	private String fieldName;

	/** The list of foreign key columns for the relationship. */
	private List<String> fkColumns = null;

	/** The list of primary key columns for the relationship. */
	private List<String> pkColumns = null;

	/** The join table information for the relationship, if applicable. */
	private String joinTable;

	/**
	 * Additional semantic information about the relationship. This value is
	 * indirectly provided by the {@link AIRelation} annotation.
	 */
	private String relationSemantic = null;

	/** Default constructor. */
	public AIEntityJoinGraph() {
	}

	/**
	 * Constructor with all properties.
	 * 
	 * @param fromEntity the source entity of the relationship
	 * @param fromTable  the source table of the relationship
	 * @param toEntity   the target entity of the relationship
	 * @param toTable    the target table of the relationship
	 * @param type       the type of relationship (e.g., "one-to-many",
	 *                   "many-to-one")
	 * @param fieldName  the field name of the relationship
	 * @param fkColumns  the list of foreign key columns for the relationship
	 * @param pkColumns  the list of primary key columns for the relationship
	 * @param joinTable  the join table information for the relationship, if
	 *                   applicable
	 */
	public AIEntityJoinGraph(String fromEntity, String fromTable, String toEntity, String toTable,
			String type, String fieldName, List<String> fkColumns, List<String> pkColumns, String joinTable) {
		this.fromEntity = fromEntity;
		this.fromTable = fromTable;
		this.toEntity = toEntity;
		this.toTable = toTable;
		this.type = type;
		this.fieldName = fieldName;
		this.fkColumns = fkColumns;
		this.pkColumns = pkColumns;
		this.joinTable = joinTable;
	}

	/**
	 * Returns the source entity of the relationship.
	 * 
	 * @return the source entity of the relationship
	 */
	public String getFromEntity() {
		return fromEntity;
	}

	/**
	 * Sets the source entity of the relationship.
	 * 
	 * @param fromEntity the source entity of the relationship
	 */
	public void setFromEntity(String fromEntity) {
		this.fromEntity = fromEntity;
	}

	/**
	 * Returns the source table of the relationship.
	 * 
	 * @return the source table of the relationship
	 */
	public String getFromTable() {
		return fromTable;
	}

	/**
	 * Sets the source table of the relationship.
	 * 
	 * @param fromTable the source table of the relationship
	 */
	public void setFromTable(String fromTable) {
		this.fromTable = fromTable;
	}

	/**
	 * Returns the target entity of the relationship.
	 * 
	 * @return the target entity of the relationship
	 */
	public String getToEntity() {
		return toEntity;
	}

	/**
	 * Sets the target entity of the relationship.
	 * 
	 * @param toEntity the target entity of the relationship
	 */
	public void setToEntity(String toEntity) {
		this.toEntity = toEntity;
	}

	/**
	 * Returns the target table of the relationship.
	 * 
	 * @return the target table of the relationship
	 */
	public String getToTable() {
		return toTable;
	}

	/**
	 * Sets the target table of the relationship.
	 * 
	 * @param toTable the target table of the relationship
	 */
	public void setToTable(String toTable) {
		this.toTable = toTable;
	}

	/**
	 * Returns the type of relationship (e.g., "one-to-many", "many-to-one").
	 * 
	 * @return the type of relationship
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type of relationship (e.g., "one-to-many", "many-to-one").
	 * 
	 * @param type the type of relationship
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the field name of the relationship.
	 * 
	 * @return the field name of the relationship
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Sets the field name of the relationship.
	 * 
	 * @param fieldName the field name of the relationship
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Returns the list of foreign key columns for the relationship.
	 * 
	 * @return the list of foreign key columns for the relationship
	 */
	public List<String> getFkColumns() {
		if (this.fkColumns == null)
			this.fkColumns = new ArrayList<>();
		return this.fkColumns;
	}

	/**
	 * Sets the list of foreign key columns for the relationship.
	 * 
	 * @param fkColumns the list of foreign key columns for the relationship
	 */
	public void setFkColumns(List<String> fkColumns) {
		this.fkColumns = fkColumns;
	}

	/**
	 * Returns the list of primary key columns for the relationship.
	 * 
	 * @return the list of primary key columns for the relationship
	 */
	public List<String> getPkColumns() {
		if (this.pkColumns == null)
			this.pkColumns = new ArrayList<>();
		return this.pkColumns;
	}

	/**
	 * Sets the list of primary key columns for the relationship.
	 * 
	 * @param pkColumns the list of primary key columns for the relationship
	 */
	public void setPkColumns(List<String> pkColumns) {
		this.pkColumns = pkColumns;
	}

	/**
	 * Returns the join table information for the relationship, if applicable.
	 * 
	 * @return the join table information for the relationship, if applicable
	 */
	public String getJoinTable() {
		return joinTable;
	}

	/**
	 * Sets the join table information for the relationship, if applicable.
	 * 
	 * @param joinTable the join table information for the relationship, if
	 *                  applicable
	 */
	public void setJoinTable(String joinTable) {
		this.joinTable = joinTable;
	}

	/**
	 * Returns the additional semantic information about the relationship.
	 * 
	 * @return the additional semantic information about the relationship
	 */
	public String getRelationSemantic() {
		return relationSemantic;
	}

	/**
	 * Sets the additional semantic information about the relationship.
	 * 
	 * @param relationSemantic the additional semantic information about the
	 *                         relationship
	 */
	public void setRelationSemantic(String relationSemantic) {
		this.relationSemantic = relationSemantic;
	}

}
