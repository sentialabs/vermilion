package nl.dimario.calc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import nl.dimario.numbercalc.JsonDataSource;

public class JsonDataSourceTest {

	private static final String TESTDATA = "{\n" + 
			"    \"aa\" : 10,\n" + 
			"    \"bb\" : {\n" + 
			"    	\"cc\" : \"this is text\",\n" + 
			"    	\"dd\" : 20\n" + 
			"    },\n" + 
			"    \"ee\": [\n" + 
			"    	100,\n" + 
			"    	200,\n" + 
			"    	\"more text\",\n" + 
			"    	300\n" + 
			"    ],\n" + 
			"    \"ff\" : true\n" + 
			"}";

	@Test
	public void testGetValues() {
		
		try {
			JsonDataSource jds = new JsonDataSource();
			Map<String,Number> map = jds.getValues( TESTDATA);
			
			assertEquals(10, map.get("aa"));
			assertNull(map.get("bb.cc"));
			assertEquals(20, map.get("bb.dd"));
			assertEquals(200, map.get("ee[1]"));
			
		} catch(Exception x) {
			x.printStackTrace();
		}
	}
}
