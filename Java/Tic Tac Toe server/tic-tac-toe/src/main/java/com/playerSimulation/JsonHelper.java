package com.playerSimulation;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonHelper {
	public static String extractFromResponse(String response, String objectName) {
	    JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
	    return jsonObject.get(objectName).getAsString();
	}
}
