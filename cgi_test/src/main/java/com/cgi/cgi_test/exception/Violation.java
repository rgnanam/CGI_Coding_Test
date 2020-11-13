package com.cgi.cgi_test.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class Violation implements Serializable {
    private static final long serialVersionUID = -4059383362760929830L;
    private String field;
    private String message;
}
