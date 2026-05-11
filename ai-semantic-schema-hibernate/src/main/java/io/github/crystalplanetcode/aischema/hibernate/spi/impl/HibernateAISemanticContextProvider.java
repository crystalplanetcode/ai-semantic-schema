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

package io.github.crystalplanetcode.aischema.hibernate.spi.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.crystalplanetcode.aischema.core.model.AIEntitySchema;
import io.github.crystalplanetcode.aischema.hibernate.HibernateSchemaAIExtractor;
import io.github.crystalplanetcode.aischema.hibernate.spi.AISemanticContext;
import io.github.crystalplanetcode.aischema.hibernate.spi.AISemanticContextConfig;
import io.github.crystalplanetcode.aischema.hibernate.spi.AISemanticContextProvider;
import io.github.crystalplanetcode.aischema.hibernate.utils.TimingUtils;

/**
 * Default implementation of {@link AISemanticContextProvider} that uses
 * {@link HibernateSchemaAIExtractor} to build the AI semantic context from
 * Hibernate entity metadata. This implementation processes the specified entity
 * classes, extracts their schemas using the provided extractor, and constructs
 * an {@link AISemanticContext} based on the extracted schemas.
 * 
 * @author Marcin Nowicki
 */
public class HibernateAISemanticContextProvider implements AISemanticContextProvider {

	private static final Logger TIMING_LOG = LoggerFactory.getLogger("io.github.crystalplanetcode.aischema.timing");

	/**
	 * The extractor used to build the AI semantic context from JPA entity metadata
	 * (utilizing Hibernate) and AI annotations.
	 */
	private final HibernateSchemaAIExtractor extractor;

	/**
	 * Flag indicating whether to exclude relational fields (e.g. associations) from
	 * the extracted entity schemas. This can be useful to focus the context on
	 * non-relational attributes or to avoid including complex relationships in the
	 * context. The relationships will still remain exposed to LLMs on top level of
	 * the context regrardless of this flag.
	 */
	private boolean excludeEntityRelationalFields = false;

	/**
	 * Creates a new {@link HibernateAISemanticContextProvider} with the specified
	 * extractor and configuration options.
	 * 
	 * @param extractor                     the extractor to use for building the
	 *                                      context
	 * @param excludeEntityRelationalFields whether to exclude relational
	 *                                      fields from the extracted entity schemas
	 *                                      on entity level (relationships will
	 *                                      still be exposed on top level of the
	 *                                      context)
	 */
	public HibernateAISemanticContextProvider(HibernateSchemaAIExtractor extractor,
			boolean excludeEntityRelationalFields) {
		this.extractor = extractor;
		this.excludeEntityRelationalFields = excludeEntityRelationalFields;
	}

	/**
	 * Creates a new {@link HibernateAISemanticContextProvider} with the specified
	 * extractor and default configuration options.
	 * 
	 * @param entityClasses the entity classes to include in the context
	 * @return a new instance of {@link AISemanticContext} built from the specified
	 *         entity classes
	 */
	@Override
	public AISemanticContext buildContext(Class<?>... entityClasses) {
		AISemanticContextConfig config = AISemanticContextConfig.builder()
				.entityClasses(entityClasses)
				.build();

		return buildContext(config);
	}

	/**
	 * Builds an {@link AISemanticContext} based on the provided configuration. The
	 * context will be constructed by analyzing the Hibernate metadata of the
	 * specified entity classes and extracting relevant semantic information.
	 * 
	 * @param config {@link AISemanticContextConfig} the configuration for building
	 *               the context
	 * @return the constructed {@link AISemanticContext}
	 */
	@Override
	public AISemanticContext buildContext(AISemanticContextConfig config) {
		List<AIEntitySchema> schemas = new ArrayList<AIEntitySchema>();
		List<Class<?>> classes = config.getEntityClasses() == null ? List.of() : config.getEntityClasses();

		TimingUtils.measure(() -> {

			for (Class<?> entityClass : classes) {
				AIEntitySchema schema = extractor.extractEntitySchema(entityClass, this.excludeEntityRelationalFields);
				schemas.add(schema);
			}

		}, TIMING_LOG, "Extracting AI semantic context");

		return new AISemanticContext(schemas);
	}
}
