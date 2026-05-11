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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for defining a single AI metric on an entity. This annotation can
 * be used to specify a specific metric related to an entity, such as
 * aggregation metrics, accuracy, or any other relevant measurement that can
 * help the AI model understand the characteristics of the entity. The metric
 * defined using this annotation will be included in the AI Semantic Schema and
 * can be utilized during prompt generation to provide better context and
 * understanding for the AI model. This annotation is repeatable and can be used
 * multiple times on the same entity to define multiple metrics, which will be
 * grouped together in the AI Semantic Schema under the {@link AIMetrics}
 * annotation.
 * 
 * @author Marcin Nowicki
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AIMetrics.class)
public @interface AIMetric {

	/**
	 * The name of the metric. This should be a concise identifier for the metric,
	 * such as "total_revenue", "average_order_value".
	 * The name is used to reference the metric in the AI Semantic Schema and can be
	 * utilized during prompt generation to provide specific information about the
	 * entity.
	 * 
	 * @return the name of the metric
	 */
	String name() default "";

	/**
	 * The label of the metric. This should be a human-readable identifier or
	 * reference for the metric, such as "Total revenue", "Average Order Value".
	 * The label is used to provide a more descriptive name for the metric in the AI
	 * Semantic Schema and can be utilized during prompt generation to provide
	 * better context and understanding for the AI model.
	 * 
	 * @return the label of the metric
	 */
	String label() default "";

	/**
	 * The description of the metric. This should provide a detailed explanation of
	 * the metric, including its purpose, calculation method, and any other relevant
	 * information. e.g.: "Total revenue is calculated by summing all sales
	 * transactions within a given period." or "Average order value is calculated by
	 * dividing total revenue by the number of orders.".
	 * The description is used to provide additional context and understanding for
	 * the AI model when working with the metric.
	 * 
	 * @return the description of the metric
	 */
	String description() default "";

	/**
	 * An array of aliases for the metric. These can be alternative names or
	 * identifiers that can be used to reference the metric in the AI Semantic
	 * Schema. Aliases can be utilized during prompt generation to provide better
	 * context and understanding for the AI model.
	 * 
	 * @return an array of aliases for the metric
	 */
	String[] aliases() default {};

	/**
	 * The aggregation method for the metric. This should specify how the metric
	 * is aggregated, such as "SUM", "AVERAGE", or "COUNT". The aggregation method
	 * is used to provide additional context and understanding for the AI model
	 * when working with the metric.
	 * 
	 * @return the aggregation method for the metric
	 */
	String aggregation() default "";

	/**
	 * The field associated with the metric. This should specify the actual EXISTING
	 * field or attribute of the entity that the metric is based on. The field is
	 * used to provide additional context and understanding for the AI model when
	 * working with the metric.
	 * 
	 * @return the field associated with the metric
	 */
	String field() default "";
}
