package com.cgi.cgi_test.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CGIBankOperationException extends Exception{
	String errorCode;
	String errorMsg;
	public CGIBankOperationException(String errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

}
