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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration object for building an {@link AISemanticContext}. This class
 * encapsulates various options that can influence how the context is
 * constructed from Hibernate metadata. For example, it allows specifying which
 * entity classes to include in the context, whether to refine the context with
 * additional information, and other potential settings that may be added in the
 * future.
 * 
 * @author Marcin Nowicki
 */
public final class AISemanticContextConfig {

	/** The entity classes to include in the context. */
	private final List<Class<?>> entityClasses;

	/**
	 * Creates a new {@link AISemanticContextConfig} based on the provided builder.
	 * 
	 * @param builder the builder to use for creating the configuration
	 */
	private AISemanticContextConfig(Builder builder) {
		this.entityClasses = List.copyOf(builder.entityClasses);
	}

	/**
	 * Returns the list of entity classes to include in the context.
	 * 
	 * @return the list of entity classes
	 */
	public List<Class<?>> getEntityClasses() {
		return entityClasses;
	}

	/**
	 * Creates a new builder for constructing an {@link AISemanticContextConfig}.
	 * 
	 * @return a new builder instance
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder class for constructing an {@link AISemanticContextConfig}.
	 * This builder provides a fluent API for setting various configuration options
	 * before building the final configuration object.
	 */
	public static final class Builder {

		/** The entity classes to include in the context. */
		private final List<Class<?>> entityClasses = new ArrayList<>();

		/** Default constructor. */
		private Builder() {
		}

		/**
		 * Sets the entity classes to include in the context.
		 * 
		 * @param entityClasses the entity classes to include
		 * @return the builder instance
		 */
		public Builder entityClasses(Class<?>... entityClasses) {
			this.entityClasses.clear();
			this.entityClasses.addAll(Arrays.asList(entityClasses));
			return this;
		}

		/**
		 * Builds the {@link AISemanticContextConfig} instance based on the current
		 * state of the builder.
		 * 
		 * @return a new {@link AISemanticContextConfig} instance
		 */
		public AISemanticContextConfig build() {
			return new AISemanticContextConfig(this);
		}
	}
}
