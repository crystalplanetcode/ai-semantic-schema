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

package io.github.crystalplanetcode.aischema.hibernate;

/**
 * Represents an FK→PK relationship extracted entirely from Hibernate's runtime
 * mapping metadata. Every field reflects a real DDL construct — no heuristics.
 *
 * <ul>
 * <li>MANY_TO_ONE / ONE_TO_ONE: fkColumns live in fromTable, pkColumns in
 * toTable.
 * <li>MANY_TO_MANY: fkColumns and pkColumns are both inside the intermediate
 * joinTable.
 * </ul>
 * 
 * @param fromEntity the Hibernate entity name of the owning side (e.g.
 *                   "com.example.domain.Invoice")
 * @param fromTable  the physical table name of the owning side (e.g. "invoice")
 * @param toEntity   the Hibernate entity name of the referenced side (e.g.
 *                   "com.example.domain.SalesOrder")
 * @param toTable    the physical table name of the referenced side (e.g.
 *                   "sales_order")
 * @param type       the relationship kind: MANY_TO_ONE | ONE_TO_ONE | MANY_TO
 *                   MANY
 * @param fieldName  the Java field name on the owning entity that declares the
 *                   association
 * @param fkColumns  the FK column name(s) on fromTable (or in joinTable for
 *                   MANY_TO_MANY)
 * @param pkColumns  the PK column name(s) on toTable (or in joinTable for
 *                   MANY_TO_MANY)
 * @param joinTable  the intermediate join-table name, non-null only for
 *                   MANY_TO_MANY
 * 
 * @see HibernateSchemaAIExtractor#buildJoinGraph(Class<?>)
 *      for details on how these are extracted from Hibernate's metadata.
 * 
 * @author Marcin Nowicki
 */
public record HibernateJoinEdge(

		/**
		 * Hibernate entity name of the owning side
		 * 
		 * @param fromEntity the Hibernate entity name of the owning side
		 */
		String fromEntity,

		/**
		 * Physical table name of the owning side (e.g. "invoice").
		 */
		String fromTable,

		/**
		 * Hibernate entity name of the referenced side (e.g.
		 * "com.example.domain.SalesOrder").
		 */
		String toEntity,

		/**
		 * Physical table name of the referenced side (e.g. "sales_order").
		 */
		String toTable,

		/**
		 * Relationship kind: MANY_TO_ONE | ONE_TO_ONE | MANY_TO_MANY.
		 */
		String type,

		/**
		 * Java field name on the owning entity that declares the association.
		 * 
		 */
		String fieldName,

		/**
		 * FK column name(s) on fromTable (or in joinTable for MANY_TO_MANY).
		 * 
		 */
		String[] fkColumns,

		/**
		 * PK column name(s) on toTable (or in joinTable for MANY_TO_MANY).
		 * 
		 */
		String[] pkColumns,

		/**
		 * Intermediate join-table name, non-null only for MANY_TO_MANY.
		 * 
		 */
		String joinTable) {
}
