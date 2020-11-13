package com.cgi.cgi_test.exception;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;


public class Errors implements Serializable {
    private static final long serialVersionUID = 3843309557111694393L;
    
    private String code;
    private String description;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<Violation> validations = new HashSet<>();
    
    public Errors() {
        //default constructor
    }
    
    public Errors(String code, String description) {
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
    
    public Set<Violation> getValidations() {
        return validations;
    }
    
    public void setValidations(Set<Violation> validations) {
        this.validations = validations;
    }
    
}
