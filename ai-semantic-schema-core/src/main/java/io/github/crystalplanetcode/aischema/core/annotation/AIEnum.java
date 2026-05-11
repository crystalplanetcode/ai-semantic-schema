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
 * Annotation for defining enum values on a field. This annotation can be used
 * to specify the possible enum values for a field, along with their
 * descriptions. The enum values defined using this annotation will be included
 * in the AI Semantic Schema and can be utilized during prompt generation to
 * provide better context and understanding for the AI model when working with
 * fields that have a limited set of possible values, such as status fields,
 * category fields, or any other fields that can be represented as enums.
 * 
 * @author Marcin Nowicki
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIEnum {

	/**
	 * An array of Entry annotations that define the enum values for the field. Each
	 * Entry annotation should specify a key, which is a concise identifier for the
	 * enum value, and a description, which provides detailed information about the
	 * enum value for better context and understanding for the AI model.
	 * 
	 * @return an array of {@link Entry} annotations representing the enum values
	 *         for the field
	 */
	Entry[] value();

	/**
	 * Inner annotation for defining a single enum value. This annotation is used
	 * within the {@link AIEnum} annotation to specify individual enum values and
	 * their descriptions. Each Entry annotation should have a key, which is a
	 * concise identifier for the enum value, and a description, which provides
	 * detailed information about the enum value for better context and
	 * understanding for the AI model.
	 */
	@interface Entry {

		/**
		 * The key of the enum value. This should be a concise identifier for the
		 * enum value, such as "PENDING", "APPROVED", or "REJECTED". The key is
		 * used to reference the enum value in the AI Semantic Schema and can be
		 * utilized during prompt generation to provide specific information about the
		 * field's possible values.
		 * 
		 * @return the key of the enum value
		 */
		String key();

		/**
		 * The description of the enum value. This should provide a detailed
		 * explanation of the enum value, including its meaning, usage, and any other
		 * relevant information. The description is used to provide additional context
		 * and understanding for the AI model when working with fields that have
		 * limited possible values.
		 * 
		 * @return the description of the enum value
		 */
		String description();
	}
}
