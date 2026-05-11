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
 * Annotation for defining AI field metadata on an entity's field. This
 * annotation can be used to specify various attributes of a field, such as its
 * name, description, aliases, examples, and whether it is aggregatable or
 * filterable. The metadata defined using this annotation will be included in
 * the AI Semantic Schema and can be utilized during prompt generation to
 * provide better context and understanding for the AI model when working with
 * the entity's fields.
 * 
 * @author Marcin Nowicki
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIField {

	/**
	 * The name of the field. This should be a concise identifier for the field,
	 * such as "Quantity", "Item price", or "Order status".
	 * The name is used to reference the field in the AI Semantic Schema and can be
	 * utilized during prompt generation to provide specific information about the
	 * entity.
	 * 
	 * @return the name of the field
	 */
	String value();

	/**
	 * The description of the field. This should provide a detailed explanation of
	 * the field, including its purpose, usage, and any other relevant information.
	 * e.g.: "Quantity of units in the production process." or "Current lifecycle
	 * status of the production order. It indicates the progress of the order."
	 * The description is used to provide additional context and understanding for
	 * the AI model when working with the field.
	 * 
	 * @return the description of the field
	 */
	String description() default "";

	/**
	 * An array of aliases for the field. These can be alternative names or
	 * identifiers that can be used to reference the field in the AI Semantic
	 * Schema. Aliases can be utilized during prompt generation to provide better
	 * context and understanding for the AI model.
	 * 
	 * @return an array of aliases for the field
	 */
	String[] aliases() default {};

	/**
	 * An array of example values for the field. These can be used to provide
	 * sample data or illustrate the expected format of the field. Examples can be
	 * utilized during prompt generation to provide better context and understanding
	 * for the AI model.
	 * 
	 * @return an array of example values for the field
	 */
	String[] examples() default {};

	/**
	 * Indicates whether the field is aggregatable. This can be used to specify
	 * if the field can be used in aggregation operations, such as SUM, AVERAGE,
	 * or COUNT. Aggregatable fields can be utilized during prompt generation to
	 * provide better context and understanding for the AI model.
	 * 
	 * @return true if the field is aggregatable, false otherwise
	 */
	boolean aggregatable() default false;

	/**
	 * Indicates whether the field is filterable. This can be used to specify if
	 * the field can be used in filtering operations, such as WHERE clauses in SQL
	 * or conditions in prompts. Filterable fields can be utilized during prompt
	 * generation to provide better context and understanding for the AI model.
	 * 
	 * @return true if the field is filterable, false otherwise
	 */
	boolean filterable() default false;
}
