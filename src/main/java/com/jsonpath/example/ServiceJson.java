package com.jsonpath.example;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class ServiceJson {

	private static InputStream jsonInputStream = ServiceJson.class
	        .getClassLoader()
	        .getResourceAsStream("intro_service.json");
	private static String jsonString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z")
	        .next();
	
	 public static void title() {
	        List<Map<String, Object>> dataList = JsonPath.parse(jsonString)
	            .read("$[?('Eva Green' in @['starring'])]");
	        String title = (String) dataList.get(0)
	            .get("title");
	        if(title.equals("Casino Royale"))
	        {
	        	System.out.println("Its true");
	        }
	    }
	 public static void HighestRevenueMovieTitle() {
	        DocumentContext context = JsonPath.parse(jsonString);
	        List<Object> revenueList = context.read("$[*]['box office']");
	        Integer[] revenueArray = revenueList.toArray(new Integer[0]);
	        Arrays.sort(revenueArray);

	        int highestRevenue = revenueArray[revenueArray.length - 1];
	        Configuration pathConfiguration = Configuration.builder()
	            .options(Option.AS_PATH_LIST)
	            .build();
	        List<String> pathList = JsonPath.using(pathConfiguration)
	            .parse(jsonString)
	            .read("$[?(@['box office'] == " + highestRevenue + ")]");

	        Map<String, String> dataRecord = context.read(pathList.get(0));
	        String title = dataRecord.get("title");

	        if(title.equals("Skyfall"))
	        {
	        	System.out.println("Title is Skyfall");
	        }
	    }
	 
	public static void main(String[] args) {
		title();
		HighestRevenueMovieTitle();
		

	}

}
