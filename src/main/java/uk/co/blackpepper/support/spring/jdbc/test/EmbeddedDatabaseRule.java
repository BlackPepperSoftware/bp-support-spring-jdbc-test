/*
 * Copyright 2014 Black Pepper Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.blackpepper.support.spring.jdbc.test;

import org.junit.rules.ExternalResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class EmbeddedDatabaseRule extends ExternalResource {

	private final EmbeddedDatabaseBuilder builder;

	private EmbeddedDatabase database;
	
	public EmbeddedDatabaseRule(EmbeddedDatabaseBuilder builder) {
		this.builder = builder;
	}

	public EmbeddedDatabaseRule(String... sqlResources) {
		builder = new EmbeddedDatabaseBuilder()
			// make H2 case-sensitive
			.setName("testdb;DATABASE_TO_UPPER=false")
			.setType(EmbeddedDatabaseType.H2);
			
		for (String sqlResource : sqlResources) {
			builder.addScript(sqlResource);
		}
	}

	@Override
	protected void before() {
		database = builder.build();
	}

	@Override
	protected void after() {
		if (database != null) {
			database.shutdown();
			database = null;
		}
	}
	
	public EmbeddedDatabase getDatabase() {
		return database;
	}
}
