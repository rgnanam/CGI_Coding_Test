package com.cgi.cgi_test.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cgi.cgi_test.common.CommonUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgi.cgi_test.cachedb.BankingDBStore;
import com.cgi.cgi_test.common.Constants;
import com.cgi.cgi_test.dto.AccountRequest;
import com.cgi.cgi_test.dto.AccountResponse;
import com.cgi.cgi_test.dto.BankAccount;
import com.cgi.cgi_test.dto.Transaction;
import com.cgi.cgi_test.exception.CGIBankOperationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankingOperationsImpl implements BankingOperations {
	
	

	@Autowired
	BankingDBStore bankDBStore;

	@Override
	public boolean createAccount(AccountRequest bankingRequest) {
		log.info("Begin createAccount");
		boolean flag= false;
		try{
		BankAccount bankAccount = createBankAccount(bankingRequest);
		Transaction transaction = createTransaction(bankingRequest,Constants.CREDIT);
		bankAccount.addTransaction(transaction);
		bankDBStore.createOrUpdate(bankAccount);
		log.info("Account# "+bankAccount.getAccountNumber()+" Bank account is created successfully");
		flag=true;
		}catch(Exception e){
			log.error(e.getMessage());
			// create Email notification
			
		}
		log.info("End createAccount");
		return flag;
		
	}
	
	@Override
	public List<AccountResponse> getAccount(Integer accountNumber) throws CGIBankOperationException {
		log.info("Begin getAccount");
		List<AccountResponse> listAccountResponse = new ArrayList<>();
		if(accountNumber != null){
			BankAccount bankAccount = bankDBStore.get(accountNumber);
			if(bankAccount != null){
				listAccountResponse.add(createAccountResponse(bankAccount));
				return listAccountResponse;
			}else{
				String errorMsg =accountNumber+" account is not found in CGI service";
				log.debug(errorMsg);
				throw new CGIBankOperationException(Constants.CGIE300,errorMsg);
			}
		}else{
			listAccountResponse = bankDBStore.getAll().stream().map(account->createAccountResponse(account)).collect(Collectors.toList());
		}
		log.info("End getAccount");
		return listAccountResponse;
		
	}

	private AccountResponse createAccountResponse(BankAccount bankAccount) {
		AccountResponse accountResponse= new AccountResponse();
		accountResponse.setBankAccountNumber(bankAccount.getAccountNumber());
		accountResponse.setCustomerName(bankAccount.getName());
		accountResponse.setBalance(bankAccount.getBalance());
		
		return accountResponse;
	}

	private Transaction createTransaction(AccountRequest bankingRequest,String transactionType) {
		Transaction transaction = new Transaction();
		transaction.setTransactionId(CommonUtility.generateTransactionID(Constants.CREDIT));
		transaction.setAccountNumber(bankingRequest.getBankAccountNumber());
		transaction.setTransactionType(transactionType);
		transaction.setAmount(bankingRequest.getInitialBalance());
		transaction.setBalance(bankingRequest.getInitialBalance());
		transaction.setCreatedTime(LocalDateTime.now());
		
		return transaction;
	}

	private BankAccount createBankAccount(AccountRequest bankingRequest) {
		BankAccount bankAccount = new BankAccount();
		bankAccount.setAccountNumber(bankingRequest.getBankAccountNumber());
		bankAccount.setName(bankingRequest.getCustomerName());
		bankAccount.setBalance(bankingRequest.getInitialBalance());
		bankAccount.setCreatedTime(LocalDateTime.now());
		
		return bankAccount;
	}

	@Override
	public boolean creditTransaction(Integer accountNumber, Double amount) throws InterruptedException {
		log.info("Begin creditTransaction");
		boolean statusFlag=false;
		BankAccount bankAccount = bankDBStore.get(accountNumber);
		if(bankAccount !=null){
			statusFlag=bankAccount.creditTransaction(amount);
			bankDBStore.createOrUpdate(bankAccount);
		}else{
			log.error(accountNumber+" is invalid account number");
		}
		log.info("End creditTransaction");
		return statusFlag;
	}

	@Override
	public boolean debitTransaction(Integer accountNumber, Double amount) throws InterruptedException, CGIBankOperationException {
		log.info("Begin debitTransaction");
		boolean statusFlag=false;
		BankAccount bankAccount = bankDBStore.get(accountNumber);
		if(bankAccount !=null){
			statusFlag=bankAccount.debitTransaction(amount);
			bankDBStore.createOrUpdate(bankAccount);
		}else{
			log.error(accountNumber+" is invalid account number");
		}
		log.info("End debitTransaction");
		return statusFlag;

	}

	@Override
	public List<Transaction> transactionHistory(Integer accountNumber) {
		log.info("Begin transactionHistory");
		boolean statusFlag=false;
		List<Transaction> listTransactions=null;
		BankAccount bankAccount = bankDBStore.get(accountNumber);
		if(bankAccount !=null){
			listTransactions=bankAccount.getTransactionList();
		}else{
			log.error(accountNumber+" is invalid account number");
		}
		log.info("End transactionHistory");
		return listTransactions;
		
	}

}
