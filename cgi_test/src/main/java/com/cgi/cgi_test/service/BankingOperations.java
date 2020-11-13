package com.cgi.cgi_test.service;

import java.util.List;

import com.cgi.cgi_test.dto.AccountRequest;
import com.cgi.cgi_test.dto.AccountResponse;
import com.cgi.cgi_test.dto.Transaction;
import com.cgi.cgi_test.exception.CGIBankOperationException;

public interface BankingOperations {
	
	public boolean createAccount(AccountRequest bankingRequest);
	public boolean creditTransaction(Integer accountNumber,Double amount) throws InterruptedException;
	public boolean debitTransaction(Integer accountNumber,Double amount) throws InterruptedException,CGIBankOperationException;
	public List<Transaction> transactionHistory(Integer accountNumber);
	public List<AccountResponse> getAccount(Integer accountNumber) throws CGIBankOperationException;

}
