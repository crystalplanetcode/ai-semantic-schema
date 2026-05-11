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

import io.github.crystalplanetcode.aischema.core.annotation.AIRelation;

/**
 * Represents the semantic information for a relationship of an AI entity,
 * including the field name, the kind of relationship, and any additional hints
 * provided by the {@link AIRelation} annotation.
 * This class is used to enrich the AI Semantic Schema with human-readable
 * metadata about the relationships of the entity, which can be utilized during
 * prompt generation to provide better context and understanding
 * for the AI model.
 * 
 * @param field the field name of the relationship
 * @param kind  the kind of relationship (e.g., "one-to-many", "many-to-one")
 * @param hint  additional hints or information about the relationship
 * 
 * @author Marcin Nowicki
 */
public record AIEntityRelationSemantic(

		/**
		 * The field name of the relationship.
		 */
		String field,

		/**
		 * The kind of relationship (e.g., "one-to-many", "many-to-one").
		 */
		String kind,

		/**
		 * Additional hints or information about the relationship. This value is
		 * indirectly provided by the {@link AIRelation} annotation.
		 */
		String hint) {
}
