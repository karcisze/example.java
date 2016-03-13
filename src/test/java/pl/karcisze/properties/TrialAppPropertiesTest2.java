package pl.karcisze.properties;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TrialAppPropertiesTest2 {

	@Test
	public void testInvalidProps() {
		AppPropertiesManager m = new TrialAppPropertiesManager();
		ArrayList<String> propUris = new ArrayList<String>();
		propUris.add("classpath:resources/jdbc_incomplete.json");
		AppProperties props = m.loadProps(propUris);

		assertTrue(!props.isValid());
		assertEquals("JDBC_URL, java.lang.String, jdbc:mysql://localhost/test", props.get("JDBC_URL").toString());
		assertEquals("JDBC_USERNAME, java.lang.String, ",props.get("JDBC_USERNAME").toString() );
		assertEquals("[JDBC_DRIVER, JDBC_PASSWORD, JDBC_URL, JDBC_USERNAME]", props.getKnownProperties().toString());
		assertEquals("[JDBC_PASSWORD, JDBC_USERNAME]", props.getMissingProperties().toString());
	}

	@Test
	public void testValidProps() {
		AppPropertiesManager m = new TrialAppPropertiesManager();
		ArrayList<String> propUris = new ArrayList<String>();
		propUris.add("classpath:resources/jdbc.properties");
		AppProperties props = m.loadProps(propUris);

		assertTrue(props.isValid());
		assertEquals("JDBC_URL, java.lang.String, jdbc:mysql://localhost/test", props.get("JDBC_URL").toString());
		assertEquals("JDBC_USERNAME, java.lang.String, username123",props.get("JDBC_USERNAME").toString() );
		assertEquals("[JDBC_DRIVER, JDBC_PASSWORD, JDBC_URL, JDBC_USERNAME, JPA_SHOWSQL]", props.getKnownProperties().toString());
		assertEquals("[]", props.getMissingProperties().toString());
	}

	@Test
	public void testGet() {
		AppPropertiesManager m = new TrialAppPropertiesManager();
		ArrayList<String> propUris = new ArrayList<String>();
		propUris.add("classpath:resources/jdbc.properties");
		AppProperties props = m.loadProps(propUris);
		assertEquals("JPA_SHOWSQL, java.lang.Boolean, true", props.get("JPA_SHOWSQL").toString());
		assertNull(props.get("FAKE"));
	}

	@Test
	public void testClear() {
		AppPropertiesManager m = new TrialAppPropertiesManager();
		ArrayList<String> propUris = new ArrayList<String>();
		propUris.add("classpath:resources/jdbc.properties");
		AppProperties props = m.loadProps(propUris);
		props.clear();

		assertTrue(!props.isValid());
		assertEquals("[JDBC_DRIVER, JDBC_PASSWORD, JDBC_URL, JDBC_USERNAME, JPA_SHOWSQL]", props.getMissingProperties().toString());
		assertEquals("JDBC_URL, java.lang.String, ", props.get("JDBC_URL").toString());

	}
}

