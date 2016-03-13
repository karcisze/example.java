package pl.karcisze.properties;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TrialAppPropertiesTest {

	@Test
	public void testEmpty() {
		List<String> propUris = new ArrayList<String>();
		String s = getStringForUris(propUris);
		assertEquals(s, "");
	}

	@Test
	public void testAWS_properties() {
		List<String> propUris = new ArrayList<String>();
		// propUris.add("file://C:/W/CrossOver/workspace.mars/P1/src/aws.properties");
		propUris.add("classpath:resources/aws.properties");
		String s = getStringForUris(propUris);
		assertEquals("aws_access_key, java.lang.String, AKIAJSF6XRIJNJTTTL3Q;"
				+ "aws_account_id, java.lang.Long, 12345678;"
				+ "aws_region_id, com.amazonaws.regions.Regions, US_EAST_1;"
				+ "aws_secret_key, java.lang.String, pmqnweEYvdiw7cvCdTOES48sOUvK1rGvvctBsgsa;", s);
	}

	@Test
	public void testAWS_json() {
		List<String> propUris = new ArrayList<String>();
		// propUris.add("file://C:/W/CrossOver/workspace.mars/P1/src/aws.properties");
		propUris.add("classpath:resources/aws.json");
		String s = getStringForUris(propUris);
		assertEquals("aws_access_key, java.lang.String, AKIAJSF6XRIJNJTTTL3Q;"
				+ "aws_account_id, java.lang.Long, 12345678;"
				+ "aws_region_id, com.amazonaws.regions.Regions, US_EAST_1;"
				+ "aws_secret_key, java.lang.String, pmqnweEYvdiw7cvCdTOES48sOUvK1rGvvctBsgsa;", s);
	}

	@Test
	public void testJDBC() {
		List<String> propUris = new ArrayList<String>();
		// propUris.add("file://C:/W/CrossOver/workspace.mars/P1/src/jdbc.properties");
		propUris.add("classpath:resources/jdbc.properties");
		String s = getStringForUris(propUris);
		assertEquals("JDBC_DRIVER, java.lang.String, com.mysql.jdbc.Driver;"
				+ "JDBC_PASSWORD, java.lang.String, password123;"
				+ "JDBC_URL, java.lang.String, jdbc:mysql://localhost/test;"
				+ "JDBC_USERNAME, java.lang.String, username123;" + "JPA_SHOWSQL, java.lang.Boolean, true;", s);
	}

	@Test
	public void testConfig() {
		List<String> propUris = new ArrayList<String>();
		// propUris.add("file://C:/W/CrossOver/workspace.mars/P1/src/jdbc.properties");
		propUris.add("classpath:resources/config.json");
		String s = getStringForUris(propUris);
		assertEquals("auth.endpoint.uri, java.lang.String, https://authserver/v1/auth;"
				+ "job.timeout, java.lang.Long, 3600;" + "jpa.showSql, java.lang.Boolean, false;"
				+ "score.factor, java.lang.Double, 2.4;" + "sns.broadcast.topic_name, java.lang.String, broadcast;",
				s);
	}

	// TODO test other methods of TrialAppProperties

	private String getStringForUris(List<String> propUris) {
		TrialAppPropertiesManager tapm = new TrialAppPropertiesManager();
		AppProperties tap = tapm.loadProps(propUris);
		String s = tap.toString();
		return s;
	}
}
