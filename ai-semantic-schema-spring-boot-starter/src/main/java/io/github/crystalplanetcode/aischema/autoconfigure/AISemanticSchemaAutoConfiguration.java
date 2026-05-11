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

package io.github.crystalplanetcode.aischema.autoconfigure;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import io.github.crystalplanetcode.aischema.hibernate.HibernateSchemaAIExtractor;
import io.github.crystalplanetcode.aischema.hibernate.spi.AISemanticContextProvider;
import io.github.crystalplanetcode.aischema.hibernate.spi.impl.HibernateAISemanticContextProvider;

/**
 * Auto-configuration for AI Semantic Schema integration with Hibernate JPA.
 * This configuration class is responsible for setting up the necessary beans to
 * extract AI Semantic Schema information from Hibernate's metadata and provide
 * it to the application context. It is conditionally activated based on the
 * presence of Hibernate and JPA classes, as well as the configuration
 * properties defined in {@link AISemanticSchemaProperties}.
 * 
 * @author Marcin Nowicki
 */
@AutoConfiguration(afterName = {
		"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
		"org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration"
})
@ConditionalOnClass({ EntityManagerFactory.class, HibernateSchemaAIExtractor.class })
@ConditionalOnSingleCandidate(EntityManagerFactory.class)
@EnableConfigurationProperties(AISemanticSchemaProperties.class)
@ConditionalOnProperty(prefix = "ai.semantic.schema", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AISemanticSchemaAutoConfiguration {

	/** Default constructor. */
	public AISemanticSchemaAutoConfiguration() {
	}

	/**
	 * Create a bean of {@link HibernateSchemaAIExtractor} if one is not already
	 * defined in the application context.
	 * This bean is responsible for extracting AI Semantic Schema information from
	 * Hibernate's metadata.
	 * 
	 * @param emf the {@link EntityManagerFactory} used to create the
	 *            {@link HibernateSchemaAIExtractor}
	 * @return a new instance of {@link HibernateSchemaAIExtractor}
	 */
	@Bean
	@ConditionalOnMissingBean
	HibernateSchemaAIExtractor hibernateSchemaAIExtractor(EntityManagerFactory emf) {
		return new HibernateSchemaAIExtractor(emf);
	}

	/**
	 * Create a bean of {@link AISemanticContextProvider} if one is not already
	 * defined in the application context.
	 * This bean provides the AI Semantic Schema context to the application,
	 * allowing other components to access the extracted schema information.
	 * 
	 * @param extractor  the {@link HibernateSchemaAIExtractor} used to extract the
	 *                   schema information
	 * @param properties the {@link AISemanticSchemaProperties} containing the
	 *                   configuration properties for AI Semantic Schema
	 * @return a new instance of {@link HibernateAISemanticContextProvider}
	 */
	@Bean
	@ConditionalOnMissingBean
	AISemanticContextProvider aiSemanticContextProvider(HibernateSchemaAIExtractor extractor,
			AISemanticSchemaProperties properties) {
		return new HibernateAISemanticContextProvider(extractor, properties.isExcludeEntityRelationalFields());
	}

}
