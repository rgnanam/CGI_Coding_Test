package com.cgi.cgi_test.exception;

import java.io.Serializable;


public class Violation implements Serializable {
    private static final long serialVersionUID = -4059383362760929830L;
    
    private String field;
    private String message;

    public Violation() {
        //default constructor
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
