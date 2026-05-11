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

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.MappingMetamodelImplementor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.crystalplanetcode.aischema.hibernate.HibernateSchemaAIExtractor;
import io.github.crystalplanetcode.aischema.hibernate.spi.AISemanticContextProvider;

class AISemanticSchemaAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(AISemanticSchemaAutoConfiguration.class))
			.withUserConfiguration(TestJpaConfig.class);

	@Test
	void createsExtractorAndProviderByDefault() {
		contextRunner.run(context -> {
			assertThat(context).hasSingleBean(HibernateSchemaAIExtractor.class);
			assertThat(context).hasSingleBean(AISemanticContextProvider.class);
		});
	}

	@Test
	void doesNotCreateBeansWhenFeatureDisabled() {
		contextRunner
				.withPropertyValues("ai.semantic.schema.enabled=false")
				.run(context -> {
					assertThat(context).doesNotHaveBean(HibernateSchemaAIExtractor.class);
					assertThat(context).doesNotHaveBean(AISemanticContextProvider.class);
				});
	}

	@Test
	void backsOffWhenUserProvidesExtractorBean() {
		contextRunner
				.withUserConfiguration(CustomExtractorConfig.class)
				.run(context -> {
					assertThat(context).hasSingleBean(HibernateSchemaAIExtractor.class);
					assertThat(context).hasSingleBean(AISemanticContextProvider.class);
					assertThat(context.getBean(HibernateSchemaAIExtractor.class))
							.isSameAs(context.getBean("customExtractor", HibernateSchemaAIExtractor.class));
				});
	}

	@Test
	void doesNotCreateBeansWhenMultipleEntityManagerFactoriesExist() {
		new ApplicationContextRunner()
				.withConfiguration(AutoConfigurations.of(AISemanticSchemaAutoConfiguration.class))
				.withUserConfiguration(MultipleEntityManagerFactoryConfig.class)
				.run(context -> {
					assertThat(context).doesNotHaveBean(HibernateSchemaAIExtractor.class);
					assertThat(context).doesNotHaveBean(AISemanticContextProvider.class);
				});
	}

	@Test
	void autoConfigurationRunsAfterJpaAutoConfiguration() {
		AutoConfiguration autoConfiguration = AISemanticSchemaAutoConfiguration.class
				.getAnnotation(AutoConfiguration.class);

		assertThat(autoConfiguration).isNotNull();
		assertThat(autoConfiguration.afterName())
				.contains("org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration")
				.contains("org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration");
	}

	@Configuration
	static class TestJpaConfig {

		@Bean
		EntityManagerFactory entityManagerFactory() {
			EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
			SessionFactoryImplementor sessionFactory = Mockito.mock(SessionFactoryImplementor.class);
			MappingMetamodelImplementor mappingMetamodel = Mockito.mock(MappingMetamodelImplementor.class);

			Mockito.when(emf.unwrap(SessionFactoryImplementor.class)).thenReturn(sessionFactory);
			Mockito.when(sessionFactory.getMappingMetamodel()).thenReturn(mappingMetamodel);

			return emf;
		}
	}

	@Configuration
	static class CustomExtractorConfig {

		@Bean("customExtractor")
		HibernateSchemaAIExtractor customExtractor() {
			return Mockito.mock(HibernateSchemaAIExtractor.class);
		}
	}

	@Configuration
	static class MultipleEntityManagerFactoryConfig {

		@Bean
		EntityManagerFactory firstEntityManagerFactory() {
			return mockEntityManagerFactory();
		}

		@Bean
		EntityManagerFactory secondEntityManagerFactory() {
			return mockEntityManagerFactory();
		}

		private EntityManagerFactory mockEntityManagerFactory() {
			EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
			SessionFactoryImplementor sessionFactory = Mockito.mock(SessionFactoryImplementor.class);
			MappingMetamodelImplementor mappingMetamodel = Mockito.mock(MappingMetamodelImplementor.class);

			Mockito.when(emf.unwrap(SessionFactoryImplementor.class)).thenReturn(sessionFactory);
			Mockito.when(sessionFactory.getMappingMetamodel()).thenReturn(mappingMetamodel);

			return emf;
		}
	}
}
