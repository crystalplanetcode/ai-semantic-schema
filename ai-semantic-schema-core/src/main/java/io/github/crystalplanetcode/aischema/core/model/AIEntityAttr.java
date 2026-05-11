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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.github.crystalplanetcode.aischema.core.annotation.AIEnum;
import io.github.crystalplanetcode.aischema.core.annotation.AIField;

/**
 * Represents an attribute of an entity, including its name, associated database
 * columns, type information, and any additional semantic information provided
 * by {@link AIField} annotation. This class is used to enrich the AI Semantic
 * Schema with human-readable metadata about the entity's attributes, which can
 * be utilized during prompt generation to provide better context and
 * understanding for the AI model.
 * 
 * @author Marcin Nowicki
 */
@JsonPropertyOrder({ "name", "columns", "kind", "javaType", "nullable", "targetEntity",
		"isEnum", "enumValues", "enumSemantic", "joinTable", "joinKeyColumns", "joinTargetColumns", "fieldSemantic" })
public class AIEntityAttr {

	/** The name of the attribute. */
	private String name;

	/** The list of database columns associated with the attribute. */
	private List<String> columns = null;

	/** The kind of the attribute (e.g., "basic", "relation"). */
	private String kind;

	/** The Java type of the attribute. */
	private String javaType;

	/** Whether the attribute is nullable. */
	private Boolean nullable;

	/** The target entity for relation attributes. */
	private String targetEntity;

	/** Whether the attribute is an enum. */
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private Boolean isEnum;

	/** The list of enum values if the attribute is an enum. */
	private List<String> enumValues = null;

	/** The join table for relation attributes. */
	private String joinTable;

	/** The join key columns for relation attributes. */
	private List<String> joinKeyColumns = null;

	/** The join target columns for relation attributes. */
	private List<String> joinTargetColumns = null;

	/**
	 * The semantic information for the field provided by
	 * the {@link AIField} annotation.
	 */
	private AIFieldSemantic fieldSemantic = null;

	/**
	 * The semantic information for enum values provided by
	 * the {@link AIEnum} annotation.
	 */
	private Map<String, String> enumSemantic = null;

	/** Default constructor. */
	public AIEntityAttr() {
	}

	/**
	 * Constructor with all properties.
	 * 
	 * @param name              the name of the attribute
	 * @param columns           the list of database columns associated with the
	 *                          attribute
	 * @param kind              the kind of the attribute (e.g., "basic",
	 *                          "relation")
	 * @param javaType          the Java type of the attribute
	 * @param nullable          whether the attribute is nullable
	 * @param targetEntity      the target entity for relation attributes
	 * @param isEnum            whether the attribute is an enum
	 * @param enumValues        the list of enum values if the attribute is an enum
	 * @param joinTable         the join table for relation attributes
	 * @param joinKeyColumns    the join key columns for relation attributes
	 * @param joinTargetColumns the join target columns for relation attributes
	 * @param fieldSemantic     the semantic information for the field provided by
	 *                          the {@link AIField} annotation
	 * @param enumSemantic      the semantic information for enum values provided by
	 *                          the {@link AIEnum} annotation
	 */
	public AIEntityAttr(String name, List<String> columns, String kind, String javaType, Boolean nullable,
			String targetEntity, Boolean isEnum, List<String> enumValues, String joinTable,
			List<String> joinKeyColumns, List<String> joinTargetColumns,
			AIFieldSemantic fieldSemantic, Map<String, String> enumSemantic) {
		this.name = name;
		this.columns = columns;
		this.kind = kind;
		this.javaType = javaType;
		this.nullable = nullable;
		this.targetEntity = targetEntity;
		this.isEnum = isEnum;
		this.enumValues = enumValues;
		this.joinTable = joinTable;
		this.joinKeyColumns = joinKeyColumns;
		this.joinTargetColumns = joinTargetColumns;
		this.fieldSemantic = fieldSemantic;
		this.enumSemantic = enumSemantic;
	}

	/**
	 * Returns the name of the attribute.
	 * 
	 * @return the name of the attribute
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the attribute.
	 * 
	 * @param name the name of the attribute
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the list of database columns associated with the attribute.
	 * 
	 * @return the list of database columns associated with the attribute
	 */
	public List<String> getColumns() {
		if (this.columns == null)
			this.columns = new ArrayList<>();
		return this.columns;
	}

	/**
	 * Sets the list of database columns associated with the attribute.
	 * 
	 * @param columns the list of database columns associated with the attribute
	 */
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	/**
	 * Returns the kind of the attribute (e.g., "basic", "relation").
	 * 
	 * @return the kind of the attribute
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * Sets the kind of the attribute (e.g., "basic", "relation").
	 * 
	 * @param kind the kind of the attribute
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * Returns the Java type of the attribute.
	 * 
	 * @return the Java type of the attribute
	 */
	public String getJavaType() {
		return javaType;
	}

	/**
	 * Sets the Java type of the attribute.
	 * 
	 * @param javaType the Java type of the attribute
	 */
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	/**
	 * Returns whether the attribute is nullable.
	 * 
	 * @return whether the attribute is nullable
	 */
	public Boolean getNullable() {
		return nullable;
	}

	/**
	 * Sets whether the attribute is nullable.
	 * 
	 * @param nullable whether the attribute is nullable
	 */
	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * Returns the target entity for relation attributes.
	 * 
	 * @return the target entity for relation attributes
	 */
	public String getTargetEntity() {
		return targetEntity;
	}

	/**
	 * Sets the target entity for relation attributes.
	 * 
	 * @param targetEntity the target entity for relation attributes
	 */
	public void setTargetEntity(String targetEntity) {
		this.targetEntity = targetEntity;
	}

	/**
	 * Returns whether the attribute is an enum.
	 * 
	 * @return whether the attribute is an enum
	 */
	public Boolean getIsEnum() {
		if (this.isEnum == null)
			this.isEnum = Boolean.FALSE;
		return this.isEnum;
	}

	/**
	 * Sets whether the attribute is an enum.
	 * 
	 * @param isEnum whether the attribute is an enum
	 */
	public void setIsEnum(Boolean isEnum) {
		this.isEnum = isEnum;
	}

	/**
	 * Returns the list of enum values if the attribute is an enum.
	 * 
	 * @return the list of enum values if the attribute is an enum
	 */
	public List<String> getEnumValues() {
		if (this.enumValues == null)
			this.enumValues = new ArrayList<>();
		return this.enumValues;
	}

	/**
	 * Sets the list of enum values if the attribute is an enum.
	 * 
	 * @param enumValues the list of enum values if the attribute is an enum
	 */
	public void setEnumValues(List<String> enumValues) {
		this.enumValues = enumValues;
	}

	/**
	 * Returns the join table for relation attributes.
	 * 
	 * @return the join table for relation attributes
	 */
	public String getJoinTable() {
		return joinTable;
	}

	/**
	 * Sets the join table for relation attributes.
	 * 
	 * @param joinTable the join table for relation attributes
	 */
	public void setJoinTable(String joinTable) {
		this.joinTable = joinTable;
	}

	/**
	 * Returns the join key columns for relation attributes.
	 * 
	 * @return the join key columns for relation attributes
	 */
	public List<String> getJoinKeyColumns() {
		if (this.joinKeyColumns == null)
			this.joinKeyColumns = new ArrayList<>();
		return this.joinKeyColumns;
	}

	/**
	 * Sets the join key columns for relation attributes.
	 * 
	 * @param joinKeyColumns the join key columns for relation attributes
	 */
	public void setJoinKeyColumns(List<String> joinKeyColumns) {
		this.joinKeyColumns = joinKeyColumns;
	}

	/**
	 * Returns the join target columns for relation attributes.
	 * 
	 * @return the join target columns for relation attributes
	 */
	public List<String> getJoinTargetColumns() {
		if (this.joinTargetColumns == null)
			this.joinTargetColumns = new ArrayList<>();
		return this.joinTargetColumns;
	}

	/**
	 * Sets the join target columns for relation attributes.
	 * 
	 * @param joinTargetColumns the join target columns for relation attributes
	 */
	public void setJoinTargetColumns(List<String> joinTargetColumns) {
		this.joinTargetColumns = joinTargetColumns;
	}

	/**
	 * Returns the semantic information for the field provided by the
	 * {@link AIField} annotation.
	 * 
	 * @return the semantic information for the field provided by the
	 *         {@link AIField} annotation
	 */
	public AIFieldSemantic getFieldSemantic() {
		return this.fieldSemantic;
	}

	/**
	 * Sets the semantic information for the field provided by
	 * the {@link AIField} annotation.
	 * 
	 * @param fieldSemantic the semantic information for the field
	 */
	public void setFieldSemantic(AIFieldSemantic fieldSemantic) {
		this.fieldSemantic = fieldSemantic;
	}

	/**
	 * Returns the semantic information for enum values provided by
	 * the {@link AIEnum} annotation.
	 * 
	 * @return the semantic information for enum values provided by the
	 *         {@link AIEnum} annotation
	 */
	public Map<String, String> getEnumSemantic() {
		if (this.enumSemantic == null)
			this.enumSemantic = new LinkedHashMap<>();
		return this.enumSemantic;
	}

	/**
	 * Sets the semantic information for enum values provided by
	 * the {@link AIEnum} annotation.
	 * 
	 * @param enumSemantic the semantic information for enum values provided by the
	 *                     {@link AIEnum} annotation
	 */
	public void setEnumSemantic(Map<String, String> enumSemantic) {
		this.enumSemantic = enumSemantic;
	}
}
