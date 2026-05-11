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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.github.crystalplanetcode.aischema.core.annotation.AIField;

/**
 * Represents the identifier information for an entity provided by
 * {@link AIField} annotation of the identifier, including the property name,
 * associated database columns, and any additional semantic information
 * about the identifier. This class is used to enrich the AI Semantic Schema
 * with human-readable metadata about the entity's identifier, which can be
 * utilized during prompt generation to provide better context and understanding
 * for the AI model.
 * 
 * @author Marcin Nowicki
 */
@JsonPropertyOrder({ "property", "columns", "identifierSemantic" })
public class AIEntityIdentifier {

	/** The property name of the identifier. */
	private String property;

	/** The list of database columns associated with the identifier. */
	private List<String> columns = null;

	/** Additional semantic information about the identifier. */
	private String identifierSemantic = null;

	/** Default constructor. */
	public AIEntityIdentifier() {
	}

	/**
	 * Constructor with all properties.
	 * 
	 * @param property           the property name of the identifier
	 * @param columns            the list of database columns associated with the
	 *                           identifier
	 * @param identifierSemantic additional semantic information about the
	 *                           identifier
	 */
	public AIEntityIdentifier(String property, List<String> columns, String identifierSemantic) {
		this.property = property;
		this.columns = columns;
		this.identifierSemantic = identifierSemantic;
	}

	/**
	 * Returns the property name of the identifier.
	 * 
	 * @return the property name of the identifier
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Sets the property name of the identifier.
	 * 
	 * @param property the property name of the identifier
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * Returns the list of database columns associated with the identifier.
	 * 
	 * @return the list of database columns associated with the identifier
	 */
	public List<String> getColumns() {
		if (this.columns == null)
			this.columns = new ArrayList<>();
		return this.columns;
	}

	/**
	 * Sets the list of database columns associated with the identifier.
	 * 
	 * @param columns the list of database columns associated with the identifier
	 */
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	/**
	 * Returns the additional semantic information about the identifier.
	 * 
	 * @return the additional semantic information about the identifier
	 */
	public String getIdentifierSemantic() {
		return identifierSemantic;
	}

	/**
	 * Sets the additional semantic information about the identifier.
	 * 
	 * @param identifierSemantic the additional semantic information about the
	 *                           identifier
	 */
	public void setIdentifierSemantic(String identifierSemantic) {
		this.identifierSemantic = identifierSemantic;
	}
}
