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
