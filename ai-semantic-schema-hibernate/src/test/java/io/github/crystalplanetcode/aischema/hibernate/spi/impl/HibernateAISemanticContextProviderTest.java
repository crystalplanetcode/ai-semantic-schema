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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import io.github.crystalplanetcode.aischema.core.model.AIEntitySchema;
import io.github.crystalplanetcode.aischema.hibernate.HibernateSchemaAIExtractor;
import io.github.crystalplanetcode.aischema.hibernate.spi.AISemanticContext;
import io.github.crystalplanetcode.aischema.hibernate.spi.AISemanticContextConfig;

/**
 * Test class for {@link HibernateAISemanticContextProvider}.
 * 
 * @author Marcin Nowicki
 */
class HibernateAISemanticContextProviderTest {

	@Test
	void buildContextFromVarargsShouldUseConfiguredExcludeFlag() {
		HibernateSchemaAIExtractor extractor = mock(HibernateSchemaAIExtractor.class);
		HibernateAISemanticContextProvider provider = new HibernateAISemanticContextProvider(extractor, true);

		AIEntitySchema schema = new AIEntitySchema();
		schema.setEntity("sample.SampleEntity");
		schema.setTable("sample_entity");
		when(extractor.extractEntitySchema(SampleEntity.class, true)).thenReturn(schema);

		AISemanticContext context = provider.buildContext(SampleEntity.class);

		verify(extractor).extractEntitySchema(SampleEntity.class, true);
		assertEquals(1, context.getProcessedSchemas().size());
		assertEquals("sample.SampleEntity", context.getProcessedSchemas().get(0).getEntity());
	}

	@Test
	void buildContextShouldReturnEmptyContextWhenNoEntitiesConfigured() {
		HibernateSchemaAIExtractor extractor = mock(HibernateSchemaAIExtractor.class);
		HibernateAISemanticContextProvider provider = new HibernateAISemanticContextProvider(extractor, false);

		AISemanticContextConfig config = AISemanticContextConfig.builder().build();
		AISemanticContext context = provider.buildContext(config);

		assertTrue(context.getProcessedSchemas().isEmpty());
		assertTrue(context.getSemanticSchema().getMetrics().isEmpty());
	}

	private static final class SampleEntity {
	}
}
