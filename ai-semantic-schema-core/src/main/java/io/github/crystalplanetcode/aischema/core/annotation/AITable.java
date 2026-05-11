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
 * Annotation to provide semantic information about a database table. This
 * annotation can be applied to classes representing database tables to enrich
 * the AI Semantic Schema with human-readable metadata, such as a label,
 * description, aliases, and granularity.
 * The information provided by this annotation can be utilized during prompt
 * generation to give better context and understanding for the AI model when
 * working with the associated tables.
 * 
 * @author Marcin Nowicki
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AITable {

	/**
	 * A brief, human-understandable (and LLM-understandable) short description of
	 * the table. Think of it as a label that conveys the essence of the table's
	 * content and purpose in a concise manner. For example, if the table represents
	 * customer orders, a suitable label might be "Customer Orders" or "Orders".
	 * This label is intended to provide a clear and intuitive name for the table
	 * that can be easily understood by both humans and language models, enhancing
	 * the clarity and usability of the AI Semantic Schema.
	 * The label should be concise yet descriptive enough to convey the main purpose
	 * of the table, making it easier for AI models to understand the context and
	 * relevance of the table when generating prompts or processing queries.
	 * 
	 * @return the label of the table
	 */
	String value();

	/**
	 * A longer, human-understandable (and LLM-understandable) description of the
	 * table. This description provides more detailed information about the table's
	 * content, purpose, and context, helping AI models and humans to better
	 * understand the table's role within the database schema. For example, for a
	 * table labeled "Customer Orders", the description might be "This table
	 * contains all customer orders, including order details, customer information,
	 * and order status."The description should be informative and provide enough
	 * context to clarify the table's function and the type of data it holds, which
	 * can be particularly useful for AI models when generating prompts or answering
	 * questions related to the table. This field is optional, and if not provided,
	 * it will default to an empty string.
	 * The description can be used in conjunction with the label to provide a richer
	 * understanding of the table, especially in cases where the label alone may not
	 * fully capture the table's purpose or content.
	 * The description can also include information about the relationships the
	 * table has with other tables, any important constraints, or any specific use
	 * cases that are relevant to the table.
	 * Overall, the description serves as a valuable piece of metadata that enhances
	 * the AI Semantic Schema by providing a more comprehensive understanding of the
	 * table, which can lead to better prompt generation and improved interactions
	 * with AI models.
	 * The description should be written in a clear and concise manner, avoiding
	 * unnecessary jargon, and should focus on conveying the key aspects of the
	 * table that are relevant for understanding its role in the database schema.
	 * The description can also be used to provide examples of the type of data
	 * stored in the table, which can further enhance the AI model's understanding
	 * and ability to generate relevant prompts or responses based on the table's
	 * content.
	 * In summary, the description is a crucial component of the AITable annotation
	 * that enriches the AI Semantic Schema with detailed, human-readable
	 * information about the table.
	 * 
	 * @return the description of the table
	 */
	String description() default "";

	/**
	 * Alternative names or aliases for the table. This can be useful for providing
	 * additional context or for accommodating different naming conventions that may
	 * be used by different users or systems. For example, a table labeled "Customer
	 * Orders" might also be referred to as "Client Orders" or "Purchase Orders" in
	 * different contexts. Providing these aliases can help AI models recognize the
	 * table even when different terminology is used, improving the model's ability
	 * to understand and generate prompts related to the table.
	 * The aliases should be relevant and commonly used alternative names for the
	 * table, and they should be provided as an array of strings. This field is
	 * optional, and if not provided, it will default to an empty array.
	 * Including aliases can enhance the flexibility and usability of the AI
	 * Semantic Schema, as it allows for a broader range of terms to be associated
	 * with the same table, making it easier for AI models to understand and work
	 * with the table in various contexts.
	 * 
	 * @return the aliases of the table
	 */
	String[] aliases() default {};

	/**
	 * The granularity of the table, indicating the level of detail or aggregation,
	 * e.g.: "Each record represents a single production order.", "Each record
	 * represents a daily summary of production orders." This information can help
	 * AI models understand the nature of the data in the table and how it should be
	 * interpreted when generating prompts or answering questions. The granularity
	 * should be provided as a string and can include any relevant information about
	 * the level of detail in the table's records. This field is optional, and if
	 * not provided, it will default to an empty string.
	 * Providing granularity information can enhance the AI model's understanding of
	 * the table's content and how it relates to other tables, which can lead to
	 * more accurate and contextually relevant prompt generation and responses.
	 * 
	 * @return the granularity of the table
	 */
	String granularity() default "";
}
