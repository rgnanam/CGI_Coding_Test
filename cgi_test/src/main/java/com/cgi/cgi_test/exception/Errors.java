package com.cgi.cgi_test.exception;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Errors implements Serializable {
    private static final long serialVersionUID = 3843309557111694393L;
    
    private String code;
    private String description;
    private Set<Violation> validations = new HashSet<>();
    public Errors(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
