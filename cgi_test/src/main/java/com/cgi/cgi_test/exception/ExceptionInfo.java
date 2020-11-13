package com.cgi.cgi_test.exception;

public class ExceptionInfo {

    private String code;

    private String description;

    public ExceptionInfo(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
