package org.woodwhales.cloud.util;

import com.google.gson.Gson;

public class JsonTool {

	private JsonTool() {}
	
	public static String toJsonString(Object object) {
		return new Gson().toJson(object);
	}
}
