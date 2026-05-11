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

package io.github.crystalplanetcode.aischema.hibernate.utils;

import java.util.function.Supplier;

import org.slf4j.Logger;

/**
 * Utility class for measuring the execution time of code blocks and logging the
 * results using SLF4J. This class provides methods to measure both
 * {@link Supplier} and {@link Runnable} code blocks, allowing for flexible
 * timing of various operations. The timing is only performed if the provided
 * logger is set to debug level, ensuring that performance overhead is avoided
 * in production environments.
 * 
 * @author Marcin Nowicki
 */
public final class TimingUtils {

	/* Private constructor to prevent instantiation of this utility class. */
	private TimingUtils() {
	}

	/**
	 * Measures the execution time of a {@link Supplier} code block and logs the
	 * duration using the provided SLF4J logger. The timing is only performed if
	 * the logger is set to debug level. The method returns the result of the
	 * supplier.
	 * 
	 * @param <T>      the type of the result produced by the supplier
	 * @param supplier the supplier whose execution time is to be measured
	 * @param log      the SLF4J logger to use for logging the duration
	 * @param name     the name of the code block being measured, used in the log
	 *                 message
	 * @return the result of the supplier
	 */
	public static <T> T measure(Supplier<T> supplier, Logger log, String name) {
		if (!log.isDebugEnabled()) {
			return supplier.get();
		}

		long start = System.nanoTime();
		try {
			return supplier.get();
		} finally {
			long durationNs = System.nanoTime() - start;
			log.debug("{} took {} µs", name, durationNs / 1_000);
		}
	}

	/**
	 * Measures the execution time of a {@link Runnable} code block and logs the
	 * duration using the provided SLF4J logger. The timing is only performed if
	 * the logger is set to debug level.
	 * 
	 * @param runnable the runnable whose execution time is to be measured
	 * @param log      the SLF4J logger to use for logging the duration
	 * @param name     the name of the code block being measured, used in the log
	 *                 message
	 */
	public static void measure(Runnable runnable, Logger log, String name) {
		if (!log.isDebugEnabled()) {
			runnable.run();
			return;
		}

		long start = System.nanoTime();
		try {
			runnable.run();
		} finally {
			long durationNs = System.nanoTime() - start;
			log.debug("{} took {} µs", name, durationNs / 1_000);
		}
	}
}
