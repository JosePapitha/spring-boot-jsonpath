package com.jsonpath.example;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import com.jayway.jsonpath.Configuration;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;



public class ReadJson {
	
	private static String json;
    private static File jsonFile = new File("src/main/resources/online_store.json");

	private static String readFile(File file, Charset charset) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), charset);
    }

	public static void init() throws IOException {
        json = readFile(jsonFile, StandardCharsets.UTF_8);
        Map<String, String> objectMap = JsonPath.read(json, "$");
		System.out.println(objectMap);
		JSONArray jsonArray = JsonPath.read(json, "$.items.book[*]");
		System.out.println(jsonArray);
    }
	public static void main(String[] args) throws IOException {
		init();
		String response = "{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"NigelRees\",\"title\":\"SayingsoftheCentury\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"EvelynWaugh\",\"title\":\"SwordofHonour\",\"price\":12.99},{\"category\":\"fiction\",\"author\":\"HermanMelville\",\"title\":\"MobyDick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99},{\"category\":\"fiction\",\"author\":\"J.R.R.Tolkien\",\"title\":\"TheLordoftheRings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}},\"expensive\":10}";
		
		Object costprice = JsonPath.read(response, "$.store.book[*].price");
		System.out.println(costprice);
		
		//using Json Provider
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);

		String author0 = JsonPath.read(document, "$.store.book[0].author");
		String author1 = JsonPath.read(document, "$.store.book[1].author");
		
		System.out.println(author0);
		System.out.println(author1);

		
		
		
	}

}
