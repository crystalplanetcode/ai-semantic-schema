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

package io.github.crystalplanetcode.aischema.hibernate.spi;

/**
 * SPI for providing an {@link AISemanticContext} based on Hibernate entity
 * metadata. Implementations can use the provided
 * {@link AISemanticContextConfig} to customize the context building process,
 * e.g. by specifying which entity classes to include or whether to refine the
 * context with additional information.
 * 
 * @author Marcin Nowicki
 */
public interface AISemanticContextProvider {

	/**
	 * Builds an {@link AISemanticContext} based on the provided entity classes. The
	 * context will be constructed by analyzing the Hibernate metadata of the
	 * specified entity classes and extracting relevant semantic information.
	 * 
	 * @param entityClasses the entity classes to include in the context
	 * @return the constructed {@link AISemanticContext}
	 */
	AISemanticContext buildContext(Class<?>... entityClasses);

	/**
	 * Builds an {@link AISemanticContext} based on the provided configuration. The
	 * context will be constructed by analyzing the Hibernate metadata of the
	 * specified entity classes and extracting relevant semantic information.
	 * 
	 * @param config the configuration for building the context
	 * @return the constructed {@link AISemanticContext}
	 */
	AISemanticContext buildContext(AISemanticContextConfig config);
}
