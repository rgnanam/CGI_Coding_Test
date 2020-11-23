package com.cgi.cgi_test.common;

public interface Constants {
	String UNKNOWN_EXCEPTION_CD="CGIE999";
	String VALIDATION_ERROR_CD="CGIE100";
	String VALIDATION_ERRORS="Validation_Errors";
	String CREDIT = "CREDIT";
	String ACCOUNT_CREATED_MSG="Account# (%s) is created successfully";
	String ACCOUNT_NOTCREATED_MSG="Account is not created successfully";
	String CREDIT_TRANSACTION_CREATED_MSG=" %s Amount is credited to account# %s successfully";
	String CREDIT_TRANSACTION_FAILED_MSG="Credit transaction is failed";

	String DEBIT_TRANSACTION_CREATED_MSG=" %s Amount is debited from account# %s successfully";
	String DEBIT_TRANSACTION_FAILED_MSG="Debit transaction is failed";
	String DEBIT = "DEBIT";
	String CGIE301="CGIE301";
	String CGIE302="CGIE302";
	String CGIE300 = "CGIE300";
	String SUCCESS = "success";
	String FAILED = "failed";
	String IN_PROGRESS="In Progress";
	String CGIE400 = "CGIE400";
	Integer HIGHPRIORITY = 1;
	Integer LOWPRIORITY = 2;
    int MAX_RETRY = 3;
}
