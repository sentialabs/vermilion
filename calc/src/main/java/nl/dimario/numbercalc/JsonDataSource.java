package nl.dimario.numbercalc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JsonDataSource {
	
	public Map<String,Number> getValues(String data) throws JsonProcessingException, IOException {
		ObjectMapper om = new ObjectMapper();
		JsonNode root = (JsonNode) om.readTree(data);
		Map<String,Number> map = new HashMap<>();		
		traverse("", root, map);
		return map;
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
