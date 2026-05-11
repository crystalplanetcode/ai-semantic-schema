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
 * Annotation to mark a field to be ignored in the AI Semantic Schema. When a
 * field is annotated with {@link AIIgnore}, it will be excluded from the
 * semantic representation of the entity and will not be considered during
 * prompt generation or any other processing related to the AI Semantic Schema.
 * This can be useful for fields that contain sensitive information, are not
 * relevant for the AI model, or should simply be omitted from the semantic
 * context for any reason. e.g. auditable fields like "createdAt", "updatedAt",
 * or "createdBy".
 * 
 * @author Marcin Nowicki
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIIgnore {
}
