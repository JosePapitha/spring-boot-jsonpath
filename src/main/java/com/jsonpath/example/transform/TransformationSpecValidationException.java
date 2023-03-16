package com.jsonpath.example.transform;

import com.jayway.jsonpath.JsonPathException;

public class TransformationSpecValidationException extends JsonPathException {

    private static final long serialVersionUID = 1L;

	public TransformationSpecValidationException(Throwable cause) {
        super(cause);
    }

    public TransformationSpecValidationException(String message) {
        super(message);
    }
}
