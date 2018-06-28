package com.gcd.calculator.taskvalidation;

public enum Errors {
    LOAD_SCHEMA_FAILURE("Calculator. Json validation schema doesn't exist or can not be loaded. "),
    UNSUPPORTED_JSON_SCHEMA_FORMAT("Calculator. JSON schema is incorrect.");
    
    private String message;
    
    Errors(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
