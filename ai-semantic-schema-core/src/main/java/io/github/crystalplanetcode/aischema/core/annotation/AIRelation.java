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

package io.github.crystalplanetcode.aischema.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to provide semantic information about a relationship of an AI
 * entity. This annotation can be applied to fields representing relationships
 * to enrich the AI Semantic Schema with human-readable metadata, such as the
 * kind of relationship and any additional hints. The information provided by
 * this annotation can be utilized during prompt generation to give better
 * context and understanding for the AI model when working with the associated
 * relationships.
 * 
 * @author Marcin Nowicki
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIRelation {

	/**
	 * The human-understandable (and LLM-understandable) description of the
	 * relationship (e.g., "One customer has many orders", "One order belongs to one
	 * customer"). This information will be presented on top level of semantic
	 * model and used to enrich the AI Semantic Schema with detailed, human-readable
	 * metadata about the relationship, which can be utilized during prompt
	 * generation to provide better context and understanding for the AI model when
	 * working with the associated relationships. The description should clearly
	 * convey the nature of the relationship in a way that is easily understandable
	 * by both humans and language models, enhancing the clarity and usability of
	 * the AI Semantic Schema. The description should be concise yet descriptive
	 * enough to convey the main purpose of the relationship, making it easier for
	 * AI models to understand the context and relevance of the relationship when
	 * generating prompts or processing queries.
	 * 
	 * @return the human-understandable description of the relationship
	 */
	String value() default "";

}
