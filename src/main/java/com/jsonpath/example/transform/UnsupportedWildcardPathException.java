package com.jsonpath.example.transform;

public class UnsupportedWildcardPathException  extends TransformationSpecValidationException {
    public UnsupportedWildcardPathException(Throwable cause) {
        super(cause);
    }
    public UnsupportedWildcardPathException(String message) {
        super(message);
    }

}
