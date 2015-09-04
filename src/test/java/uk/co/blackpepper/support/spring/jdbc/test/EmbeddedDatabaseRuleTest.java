package uk.co.blackpepper.support.spring.jdbc.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class EmbeddedDatabaseRuleTest {

	private EmbeddedDatabaseRule rule;
	
	private final TestName testName = new TestName();
	
	@Rule
	public TestName getTestName() {
		return testName;
	}
	
	@After
	public void tearDown() {
		if (rule != null) {
			rule.after();
		}
	}
	
	@Test
	public void constructorWithBuilderCreatesDatabase() throws SQLException {
		rule = new EmbeddedDatabaseRule(new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2)
			.addScript(getTestScriptPath()));
		
		rule.before();
		executeSql(rule.getDatabase(), "SELECT * FROM x");
	}

	@Test
	public void constructorWithScriptsVarArgsCreatesDatabase() throws SQLException {
		rule = new EmbeddedDatabaseRule();
		
		rule.before();
		executeSql(rule.getDatabase());
	}
	
	@Test
	public void constructorWithScriptsVarArgsCreatesCaseSensitiveDatabase() throws SQLException {
		rule = new EmbeddedDatabaseRule(getTestScriptPath());
		
		rule.before();
		executeSql(rule.getDatabase(), "SELECT * FROM \"x\"");
	}
	
	@Test
	public void afterDestroysDatabase() {
		rule = new EmbeddedDatabaseRule();
		rule.before();
		
		rule.after();
		
		assertThat(rule.getDatabase(), is(nullValue()));
	}

	private String getTestScriptPath() {
		return String.format("uk/co/blackpepper/support/spring/jdbc/test/%s.sql", testName.getMethodName());
	}

	private static void executeSql(DataSource dataSource) throws SQLException {
		executeSql(dataSource, "SELECT 1");
	}
	
	private static void executeSql(DataSource dataSource, String sql) throws SQLException {
		dataSource.getConnection().prepareStatement(sql).execute();
	}
}
