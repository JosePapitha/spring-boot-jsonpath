package com.jsonpath.example.transform;

import com.jayway.jsonpath.JsonPathException;

public class TransformationException extends JsonPathException {

    private static final long serialVersionUID = 1L;

	public TransformationException(Throwable cause) {
        super(cause);
    }

    public TransformationException(String message) {
        super(message);
    }
}