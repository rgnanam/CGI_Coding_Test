package com.cgi.cgi_test.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class CGIDBIntegrationException extends Exception{
    private String errorCode;
    private String errorMsg;
}
