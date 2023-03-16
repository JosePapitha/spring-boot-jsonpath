/*
 * Copyright 2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jsonpath.example;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.MapFunction;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.internal.*;
import com.jayway.jsonpath.internal.path.PathCompiler;
import com.jsonpath.example.transform.*;
import com.jsonpath.example.transform.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.jayway.jsonpath.Option.ALWAYS_RETURN_LIST;
import static com.jayway.jsonpath.Option.AS_PATH_LIST;
import static com.jayway.jsonpath.internal.Utils.*;

/**
 * <p/>
 * JsonPath is to JSON what XPATH is to XML, a simple way to extract parts of a given document. JsonPath is
 * available in many programming languages such as Javascript, Python and PHP.
 * <p/>
 * JsonPath allows you to compile a json path string to use it many times or to compile and apply in one
 * single on demand operation.
 * <p/>
 * Given the Json document:
 * <p/>
 * <pre>
 * String json =
 * "{
 * "store":
 * {
 * "book":
 * [
 * {
 * "category": "reference",
 * "author": "Nigel Rees",
 * "title": "Sayings of the Century",
 * "price": 8.95
 * },
 * {
 * "category": "fiction",
 * "author": "Evelyn Waugh",
 * "title": "Sword of Honour",
 * "price": 12.99
 * }
 * ],
 * "bicycle":
 * {
 * "color": "red",
 * "price": 19.95
 * }
 * }
 * }";
 * </pre>
 * <p/>
 * A JsonPath can be compiled and used as shown:
 * <p/>
 * <code>
 * JsonPath path = JsonPath.compile("$.store.book[1]");
 * <br/>
 * List&lt;Object&gt; books = path.read(json);
 * </code>
 * </p>
 * Or:
 * <p/>
 * <code>
 * List&lt;Object&gt; authors = JsonPath.read(json, "$.store.book[*].author")
 * </code>
 * <p/>
 * If the json path returns a single value (is definite):
 * </p>
 * <code>
 * String author = JsonPath.read(json, "$.store.book[1].author")
 * </code>
 */
public class JsonPath {

    private final Path path;

    private JsonPath(String jsonPath, Predicate[] filters) {
        notNull(jsonPath, "path can not be null");
        this.path = PathCompiler.compile(jsonPath, filters);
    }

    /**
     * Returns the string representation of this JsonPath
     *
     * @return path as String
     */
    public String getPath() {
        return this.path.toString();
    }

    /**
     * @see JsonPath#isDefinite()
     */
    public static boolean isPathDefinite(String path) {
        return compile(path).isDefinite();
    }


    /**
     * Checks if a path points to a single item or if it potentially returns multiple items
     * <p/>
     * a path is considered <strong>not</strong> definite if it contains a scan fragment ".."
     * or an array position fragment that is not based on a single index
     * <p/>
     * <p/>
     * definite path examples are:
     * <p/>
     * $store.book
     * $store.book[1].title
     * <p/>
     * not definite path examples are:
     * <p/>
     * $..book
     * $.store.book[*]
     * $.store.book[1,2]
     * $.store.book[?(@.category = 'fiction')]
     *
     * @return true if path is definite (points to single item)
     */
    public boolean isDefinite() {
        return path.isDefinite();
    }

    /**
     * Checks if a path is a Function Path.
     * function path examples are:
     * <p/>
     * $.store.book[?(@.price < 10)].price.max()
     * <p/>
     * @return true if path is definite (points to single item)
     */
    public boolean isFunctionPath() {
        return path.isFunctionPath();
    }

    
    /**
     * Compiles a JsonPath
     *
     * @param jsonPath to compile
     * @param filters  filters to be applied to the filter place holders  [?] in the path
     * @return compiled JsonPath
     */
    public static JsonPath compile(String jsonPath, Predicate... filters) {
        notEmpty(jsonPath, "json can not be null or empty");

        return new JsonPath(jsonPath, filters);
    }


    // --------------------------------------------------------
    //
    // Static utility functions
    //
    // --------------------------------------------------------

    /**
     * Creates a new JsonPath and applies it to the provided Json object
     *
     * @param json     a json object
     * @param jsonPath the json path
     * @param filters  filters to be applied to the filter place holders  [?] in the path
     * @param <T>      expected return type
     * @return list of objects matched by the given path
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T read(Object json, String jsonPath, Predicate... filters) {
        return parse(json).read(jsonPath, filters);
    }

    /**
     * Creates a new JsonPath and applies it to the provided Json string
     *
     * @param json     a json string
     * @param jsonPath the json path
     * @param filters  filters to be applied to the filter place holders  [?] in the path
     * @param <T>      expected return type
     * @return list of objects matched by the given path
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T read(String json, String jsonPath, Predicate... filters) {
        return new ParseContextImpl().parse(json).read(jsonPath, filters);
    }


    /**
     * Creates a new JsonPath and applies it to the provided Json object
     *
     * @param jsonURL  url pointing to json doc
     * @param jsonPath the json path
     * @param filters  filters to be applied to the filter place holders  [?] in the path
     * @param <T>      expected return type
     * @return list of objects matched by the given path
     */
    @SuppressWarnings({"unchecked"})
    @Deprecated
    public static <T> T read(URL jsonURL, String jsonPath, Predicate... filters) throws IOException {
        return new ParseContextImpl().parse(jsonURL).read(jsonPath, filters);
    }

    /**
     * Creates a new JsonPath and applies it to the provided Json object
     *
     * @param jsonFile json file
     * @param jsonPath the json path
     * @param filters  filters to be applied to the filter place holders  [?] in the path
     * @param <T>      expected return type
     * @return list of objects matched by the given path
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T read(File jsonFile, String jsonPath, Predicate... filters) throws IOException {
        return new ParseContextImpl().parse(jsonFile).read(jsonPath, filters);
    }

    /**
     * Creates a new JsonPath and applies it to the provided Json object
     *
     * @param jsonInputStream json input stream
     * @param jsonPath        the json path
     * @param filters         filters to be applied to the filter place holders  [?] in the path
     * @param <T>             expected return type
     * @return list of objects matched by the given path
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T read(InputStream jsonInputStream, String jsonPath, Predicate... filters) throws IOException {
        return new ParseContextImpl().parse(jsonInputStream).read(jsonPath, filters);
    }


    // --------------------------------------------------------
    //
    // Static Fluent API
    //
    // --------------------------------------------------------


   
       /**
     * Parses the given JSON input using the default {@link Configuration} and
     * returns a {@link DocumentContext} for path evaluation
     *
     * @param json input
     * @return a read context
     */
    public static DocumentContext parse(Object json) {
        return new ParseContextImpl().parse(json);
    }

    /**
     * Parses the given JSON input using the default {@link Configuration} and
     * returns a {@link DocumentContext} for path evaluation
     *
     * @param json string
     * @return a read context
     */
    public static DocumentContext parse(String json) {
        return new ParseContextImpl().parse(json);
    }

    /**
     * Parses the given JSON input using the default {@link Configuration} and
     * returns a {@link DocumentContext} for path evaluation
     *
     * @param json stream
     * @return a read context
     */
    public static DocumentContext parse(InputStream json) {
        return new ParseContextImpl().parse(json);
    }

    /**
     * Parses the given JSON input using the default {@link Configuration} and
     * returns a {@link DocumentContext} for path evaluation
     *
     * @param json file
     * @return a read context
     */
    public static DocumentContext parse(File json) throws IOException {
        return new ParseContextImpl().parse(json);
    }

    /**
     * Parses the given JSON input using the default {@link Configuration} and
     * returns a {@link DocumentContext} for path evaluation
     *
     * @param json url
     * @return a read context
     */
    @Deprecated
    public static DocumentContext parse(URL json) throws IOException {
        return new ParseContextImpl().parse(json);
    }

    /**
     * Parses the given JSON input using the provided {@link Configuration} and
     * returns a {@link DocumentContext} for path evaluation
     *
     * @param json input
     * @return a read context
     */
    public static DocumentContext parse(Object json, com.jayway.jsonpath.Configuration configuration) {
        return new ParseContextImpl(configuration).parse(json);
    }

    /**
     * Parses the given JSON input using the provided {@link Configuration} and
     * returns a {@link DocumentContext} for path evaluation
     *
     * @param json input
     * @return a read context
     */
    public static DocumentContext parse(String json, com.jayway.jsonpath.Configuration configuration) {
        return new ParseContextImpl(configuration).parse(json);
    }

    /**
     * Parses the given JSON input using the provided {@link Configuration} and
     * returns a {@link DocumentContext} for path evaluation
     *
     * @param json input
     * @return a read context
     */
    public static DocumentContext parse(InputStream json, com.jayway.jsonpath.Configuration configuration) {
        return new ParseContextImpl(configuration).parse(json);
    }

    /**
     * Parses the given JSON input using the provided {@link Configuration} and
     * returns a {@link DocumentContext} for path evaluation
     *
     * @param json input
     * @return a read context
     */
    public static DocumentContext parse(File json, com.jayway.jsonpath.Configuration configuration) throws IOException {
        return new ParseContextImpl(configuration).parse(json);
    }

    /**
     * Parses the given JSON input using the provided {@link Configuration} and
     * returns a {@link DocumentContext} for path evaluation
     *
     * @param json input
     * @return a read context
     */
    @Deprecated
    public static DocumentContext parse(URL json, com.jayway.jsonpath.Configuration configuration) throws IOException {
        return new ParseContextImpl(configuration).parse(json);
    }

    private <T> T resultByConfiguration(Object jsonObject, Configuration configuration, EvaluationContext evaluationContext) {
        if(configuration.containsOption(Option.AS_PATH_LIST)){
            return (T)evaluationContext.getPathList();
        } else {
            return (T) jsonObject;
        }
    }

    private <T> T handleMissingPathInContext(final com.jayway.jsonpath.Configuration configuration) {
        boolean optAsPathList = configuration.containsOption(AS_PATH_LIST);
        boolean optAlwaysReturnList = configuration.containsOption(com.jayway.jsonpath.Option.ALWAYS_RETURN_LIST);
        if (optAsPathList) {
            return (T) configuration.jsonProvider().createArray();
        } else {
            if (optAlwaysReturnList) {
                return (T) configuration.jsonProvider().createArray();
            } else {
                return (T) (path.isDefinite() ? null : configuration.jsonProvider().createArray());
            }
        }
    }
}
