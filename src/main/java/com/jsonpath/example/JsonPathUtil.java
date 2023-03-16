package com.jsonpath.example;

import java.io.InputStream;
import java.nio.charset.Charset;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jsonpath.example.transform.*;
import com.jsonpath.example.transform.Option;
import com.jsonpath.example.transform.TransformationSpec;

public class JsonPathUtil {
	static InputStream sourceStream;
    static InputStream transformSpec;
    static Configuration configuration;
	public static void testjson() {	
	
    TransformationSpec spec;
    Object sourceJson;

    
    configuration   = Configuration.builder()
            .options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
    sourceStream = JsonPathUtil.class.getClassLoader().getResourceAsStream("transformsource.json");
    sourceJson = configuration.jsonProvider().parse(sourceStream, Charset.defaultCharset().name());

    DocumentContext jsonContext = JsonPath.parse(sourceJson);
    System.out.println("Document Input :" + jsonContext.jsonString());

    transformSpec = JsonPathUtil.class.getClassLoader().getResourceAsStream("transformspec.json");

    spec = configuration.transformationProvider().spec(transformSpec, configuration);
}

public static void main(String[] args)
{
testjson();

}
}
