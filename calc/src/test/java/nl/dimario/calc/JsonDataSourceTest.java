package nl.dimario.calc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import nl.dimario.numbercalc.JsonDataSource;

public class JsonDataSourceTest {

	@Test
	public void testGetValues() {
		
		try {
			JsonDataSource jds = new JsonDataSource( "src/test/resources/testdata.json");
			Map<String,Number> map = jds.getValues();
			
			assertEquals(10, map.get("aa"));
			assertNull(map.get("bb.cc"));
			assertEquals(20, map.get("bb.dd"));
			assertEquals(200, map.get("ee[1]"));
			
		} catch(Exception x) {
			x.printStackTrace();
		}
	}
}
