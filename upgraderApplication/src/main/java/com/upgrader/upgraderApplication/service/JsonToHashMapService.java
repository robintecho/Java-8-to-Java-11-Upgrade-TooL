package com.upgrader.upgraderApplication.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class JsonToHashMapService {
	/**
	 * Convert the json to HashMap
	 * 
	 * @param jsonString
	 * @return Map
	 * @throws Exception
	 */

	public Map ConvertToMap(String jsonString) throws Exception {
		JSONObject jsonObject = new JSONObject(jsonString);
		return getMap(jsonObject);
	}

	/**
	 * To convert the json string to HashMap using jsonobject
	 * 
	 * @param object
	 * @return Map
	 * @throws JSONException
	 */

	private static Map getMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		Object jsonObject = null;
		String key = null;
		Object value = null;

		Iterator<String> keys = object.keys();
		while (keys.hasNext()) {
			key = null;
			value = null;
			key = keys.next();
			if (null != key && !object.isNull(key)) {
				value = object.get(key);
			}
			if (value instanceof JSONObject) {
				map.put(key, getMap((JSONObject) value));
				continue;
			}
			if (value instanceof JSONArray) {
				JSONArray array = ((JSONArray) value);
				List list = new ArrayList();
				for (int i = 0; i < array.length(); i++) {
					jsonObject = array.get(i);
					if (jsonObject instanceof JSONObject) {
						list.add(getMap((JSONObject) jsonObject));
					} else {
						list.add(jsonObject);
					}
				}
				map.put(key, list);
				continue;
			}
			map.put(key, value);
		}
		return map;
	}
}
