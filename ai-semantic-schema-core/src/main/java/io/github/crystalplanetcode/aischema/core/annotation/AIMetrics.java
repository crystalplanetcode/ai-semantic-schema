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
 * Annotation for defining AI metrics on an entity. This annotation can be used
 * to specify various metrics related to an entity, such as aggregation metrics,
 * accuracy, or any other relevant measurements that can help the AI model
 * understand the characteristics of the entity. The metrics defined using this
 * annotation will be included in the AI Semantic Schema and can be utilized
 * during prompt generation to provide better context and understanding for the
 * AI model see {@link AIMetric} for more details.
 * 
 * @author Marcin Nowicki
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIMetrics {

	/**
	 * An array of AIMetric annotations that define the metrics for the entity. Each
	 * AIMetric annotation can specify a name, value, and description for a specific
	 * metric. These metrics will be included in the AI Semantic Schema and can be
	 * used to provide additional context and information about the entity during
	 * prompt generation.
	 * 
	 * @return an array of {@link AIMetric} annotations representing the metrics for
	 *         the entity
	 */
	AIMetric[] value();
}
