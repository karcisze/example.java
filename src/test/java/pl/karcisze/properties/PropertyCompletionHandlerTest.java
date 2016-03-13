package pl.karcisze.properties;

import static org.junit.Assert.*;

import org.junit.Test;

public class PropertyCompletionHandlerTest {

	@Test
	public void testEmpty() {
		PropertyCompletionHandler pm = new PropertyCompletionHandler(new String[0][0], new String[0][0]);
		assertEquals( pm.toString(), "");
		assertEquals( PropertyCompletionHandler.PROPERTY_TYPE_DEFAULT, pm.getPropertyType("ala"));
		assertEquals(null, pm.getPropertyClosure("ala"));
	}

	@Test
	public void test2() {
		String[][] propertyKeysInitial = { { "JDBC_DRIVER", "JDBC_URL", "JDBC_USERNAME", "JDBC_PASSWORD" } };
		String[][] propertyTypesInitial = { { "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String" } };
		PropertyCompletionHandler pm = new PropertyCompletionHandler(propertyKeysInitial, propertyTypesInitial);
		assertEquals("[JDBC_DRIVER, JDBC_PASSWORD, JDBC_URL, JDBC_USERNAME]", pm.getPropertyClosure("JDBC_URL").toString());
		assertEquals("java.lang.String", pm.getPropertyType("JDBC_URL"));
	}

	@Test
	public void test3() {
		String[][] propertyKeysInitial = { { "xxx", "aws_region_id", "yyy" } };
		String[][] propertyTypesInitial = { { "java.lang.String", "com.amazonaws.regions.Regions", "java.lang.String" } };
		PropertyCompletionHandler pm = new PropertyCompletionHandler(propertyKeysInitial, propertyTypesInitial);
		Object v = pm.createCompletedPropertyValue("aws_region_id", "us-east-1");
		assertEquals("com.amazonaws.regions.Regions", v.getClass().getName());
		assertEquals("US_EAST_1",v.toString());
	}

	@Test
	public void test4() {
		String[][] propertyKeysInitial = { { "JDBC_DRIVER", "yyy" } };
		String[][] propertyTypesInitial = { { "java.lang.String", "java.lang.String" } };
		PropertyCompletionHandler pm = new PropertyCompletionHandler(propertyKeysInitial, propertyTypesInitial);
		Object v = pm.createCompletedPropertyValue("JDBC_DRIVER", "jdbc:mysql://localhost/test");
		assertEquals("java.lang.String", v.getClass().getName());
		assertEquals("jdbc:mysql://localhost/test",v.toString());
	}

	@Test
	public void test5() {
		String[][] propertyKeysInitial = { { "JDBC_DRIVER", "JDBC_URL", "JDBC_USERNAME", "JDBC_PASSWORD" } };
		String[][] propertyTypesInitial = { { "java.lang.Class", "java.net.URL", "java.lang.String", "java.lang.String" } };
		PropertyCompletionHandler pm = new PropertyCompletionHandler(propertyKeysInitial, propertyTypesInitial);
		String s = pm.toString();
		assertEquals("JDBC_URL:(JDBC_DRIVER,java.lang.Class)(JDBC_PASSWORD,java.lang.String)(JDBC_URL,java.net.URL)(JDBC_USERNAME,java.lang.String)"
  			  + "JDBC_USERNAME:(JDBC_DRIVER,java.lang.Class)(JDBC_PASSWORD,java.lang.String)(JDBC_URL,java.net.URL)(JDBC_USERNAME,java.lang.String)"
			  + "JDBC_DRIVER:(JDBC_DRIVER,java.lang.Class)(JDBC_PASSWORD,java.lang.String)(JDBC_URL,java.net.URL)(JDBC_USERNAME,java.lang.String)"
			  + "JDBC_PASSWORD:(JDBC_DRIVER,java.lang.Class)(JDBC_PASSWORD,java.lang.String)(JDBC_URL,java.net.URL)(JDBC_USERNAME,java.lang.String)", s);
	}
	
	@Test
	public void test6() {
		String[][] propertyKeysInitial = { { "JDBC_DRIVER", "JDBC_URL", "JDBC_USERNAME", "JDBC_PASSWORD" }, { "hibernate.generate_statistics", "hibernate.show_sql" } };
		String[][] propertyTypesInitial = { { "java.lang.Class", "java.net.URL", "java.lang.String", "java.lang.String" }, { "java.lang.Boolean", "java.lang.Boolean" } };
		PropertyCompletionHandler pm = new PropertyCompletionHandler(propertyKeysInitial, propertyTypesInitial);
		String s = pm.toString();
		assertEquals("JDBC_URL:(JDBC_DRIVER,java.lang.Class)(JDBC_PASSWORD,java.lang.String)(JDBC_URL,java.net.URL)(JDBC_USERNAME,java.lang.String)" +
				"JDBC_USERNAME:(JDBC_DRIVER,java.lang.Class)(JDBC_PASSWORD,java.lang.String)(JDBC_URL,java.net.URL)(JDBC_USERNAME,java.lang.String)" +
				"hibernate.show_sql:(hibernate.generate_statistics,java.lang.Boolean)(hibernate.show_sql,java.lang.Boolean)" +
				"hibernate.generate_statistics:(hibernate.generate_statistics,java.lang.Boolean)(hibernate.show_sql,java.lang.Boolean)" +
				"JDBC_DRIVER:(JDBC_DRIVER,java.lang.Class)(JDBC_PASSWORD,java.lang.String)(JDBC_URL,java.net.URL)(JDBC_USERNAME,java.lang.String)" +
				"JDBC_PASSWORD:(JDBC_DRIVER,java.lang.Class)(JDBC_PASSWORD,java.lang.String)(JDBC_URL,java.net.URL)(JDBC_USERNAME,java.lang.String)", s);
	}
}
