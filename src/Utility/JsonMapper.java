package Utility;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.parser.Entity;

public class JsonMapper {
	//simple map
	public String mapToJsonString(HashMap<String, String> map)
	{
		StringBuilder json = new StringBuilder("{");
		for(Map.Entry<String, String> entity : map.entrySet()) 
		{
			String field = String.format("\"%s\": \"%s\",", entity.getKey(), entity.getValue());
			json.append(field);
		}
		json.setCharAt(json.length()-1, '}');
		return json.toString();
	}
}
