package nl.dimario.numbercalc;

/**
 * This implements the transformation of an arbitrary JSON
 * object, that may contain amongst other things numerical values,
 * into a Map that  our renderer can use to obtain the values of
 * variables by their names.
 * 
 * You can use this data source when the service that provides data
 * does not need any input (or at least, it does not need to
 * know what variable names you are interested in). It always returns
 * predictable data in the same format using fixed names for the fields.
 * 
 * Of course, the actual names of the fields and what kind of information
 * they hold must be communicated in some other way to the 
 * creators of the embedded scripting expressions.
 * 
 * The "external service" for this JsonDataSource is simple a text
 * file containing JSON data somewhere on your local file system.
 * 
 * The alternative is to let creators use any variable names they like,
 * and then use NumberVariableScanner to extract the names.
 * In this case, between parsing the input and rendering the output
 * you must interrogate the external service effectively telling it
 * "Here is a collection of variable names, please fill in the values for me".
 * 
 */
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JsonDataSource {
	
	private String fileName;
	
	public JsonDataSource( String fileName) {
		this.fileName = fileName;
	}
	
	public Map<String,Number> getValues() throws JsonProcessingException, IOException {
		
		Map<String,Number> map = new HashMap<>();
		JsonNode root = readData();
		traverse("", root, map);
		return map;
	}
	
	private JsonNode readData() throws JsonProcessingException, IOException {
		
		ObjectMapper om = new ObjectMapper();
		return (JsonNode) om.readTree(new File(fileName));
	}

	private void traverse(String nodePath, JsonNode node, Map<String,Number> map) {
		
		if( node.isObject()) {
	        Iterator<String> fieldNames = node.fieldNames();

	        while(fieldNames.hasNext()) {
	            String fieldName = fieldNames.next();
	            JsonNode fieldValue = node.get(fieldName);
	            String newPath = (nodePath.length() < 1) ? fieldName : nodePath + "." + fieldName;
	            traverse(newPath, fieldValue, map);
	        }
		} else if(node.isArray()) {
			Iterator<JsonNode> arrayItemsIterator = node.elements();
			int index = 0;
			while(arrayItemsIterator.hasNext()) {
				JsonNode item = arrayItemsIterator.next();
				String newPath = String.format( "%s[%d]", nodePath, index);
				traverse( newPath, item, map);
				index++;
			}			
		} else if( node.isValueNode()) {
			JsonNodeType type  = node.getNodeType();
			if( JsonNodeType.NUMBER.equals(type)) {
				map.put(nodePath, node.numberValue());
			}
		}
	}
}
