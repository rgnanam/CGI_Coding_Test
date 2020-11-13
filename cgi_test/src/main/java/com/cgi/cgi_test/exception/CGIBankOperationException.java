package com.cgi.cgi_test.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CGIBankOperationException extends Exception{
	private String errorCode;
	private String errorMsg;
}
