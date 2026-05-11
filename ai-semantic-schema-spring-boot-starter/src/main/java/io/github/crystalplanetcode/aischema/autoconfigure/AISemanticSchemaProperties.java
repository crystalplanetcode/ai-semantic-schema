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

package io.github.crystalplanetcode.aischema.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for AI Semantic Schema.
 * 
 * @author Marcin Nowicki
 */
@ConfigurationProperties(prefix = "ai.semantic.schema")
public class AISemanticSchemaProperties {

	/** Whether AI Semantic Schema is enabled. Default is true. */
	private boolean enabled = true;

	/**
	 * Whether to exclude entity relational fields.
	 * The relationships will be still included in the schema (owning side),
	 * defined by actuall table names and field names of database model.
	 * 
	 * Default is false
	 */
	private boolean excludeEntityRelationalFields = false;

	/** Default constructor. */
	public AISemanticSchemaProperties() {
	}

	/**
	 * Whether AI Semantic Schema is enabled.
	 * 
	 * @return true if enabled, false otherwise
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Set whether AI Semantic Schema is enabled.
	 * 
	 * @param enabled true to enable, false to disable
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Whether to exclude entity relational fields.
	 * 
	 * @return true if entity relational fields are excluded, false otherwise
	 */
	public boolean isExcludeEntityRelationalFields() {
		return excludeEntityRelationalFields;
	}

	/**
	 * Set whether to exclude entity relational fields.
	 * 
	 * @param excludeEntityRelationalFields true to exclude, false to include
	 */
	public void setExcludeEntityRelationalFields(boolean excludeEntityRelationalFields) {
		this.excludeEntityRelationalFields = excludeEntityRelationalFields;
	}

}
