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

package io.github.crystalplanetcode.aischema.core.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

/**
 * Utility class for serializing AI semantic schemas to YAML format. This class
 * uses Jackson's YAMLMapper to convert Java objects representing the semantic
 * schema into a human-readable YAML string. The serializer is configured to
 * produce compact and clean YAML output, minimizing unnecessary quotes and line
 * breaks.
 * 
 * @author Marcin Nowicki
 */
public final class AISemanticSchemaSerializerYaml {

	/** The YAML mapper used for serialization. */
	private static final YAMLMapper OBJECT_MAPPER = YAMLMapper.builder()
			.disable(YAMLGenerator.Feature.SPLIT_LINES)
			.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
			.build();

	/* Static initializer to configure the YAML mapper. */
	static {
		OBJECT_MAPPER.setDefaultPropertyInclusion(
				JsonInclude.Value.construct(
						JsonInclude.Include.NON_EMPTY,
						JsonInclude.Include.NON_EMPTY));
		OBJECT_MAPPER.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
	}

	/** Private constructor to prevent instantiation. */
	private AISemanticSchemaSerializerYaml() {
	}

	/**
	 * Serializes the given AI semantic schema object to a YAML string.
	 * 
	 * @param schema the AI semantic schema object to serialize
	 * @return the YAML representation of the schema
	 * @throws JsonProcessingException if an error occurs during serialization
	 */
	public static String serialize(Object schema) throws JsonProcessingException {
		return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
	}
}
