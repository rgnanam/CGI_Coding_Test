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
public class BankingOperationImpl implements BankingOperation{
	
	

	@Autowired
	BankingDBStore bankDBStore;

	@Override
	public boolean createAccount(AccountRequest bankingRequest) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		log.info("Begin getAccount");
		List<AccountResponse> listAccountResponse = new ArrayList<>();
		if(accountNumber != null){
			BankAccount bankAccount = bankDBStore.get(accountNumber);
			if(bankAccount != null){
				listAccountResponse.add(createAccountResponse(bankAccount));
				return listAccountResponse;
			}else{
				log.debug(accountNumber+" Account is not found");
				throw new CGIBankOperationException("CGIE300","Account is not found");
			}
		}else{
			listAccountResponse = bankDBStore.getAll().stream().map(account->createAccountResponse(account)).collect(Collectors.toList());
		}
		log.info("End getAccount");
		return listAccountResponse;
		
	}

	private AccountResponse createAccountResponse(BankAccount bankAccount) {
		// TODO Auto-generated method stub
		AccountResponse accountResponse= new AccountResponse();
		accountResponse.setBankAccountNumber(bankAccount.getAccountNumber());
		accountResponse.setCustomerName(bankAccount.getName());
		accountResponse.setBalance(bankAccount.getBalance());
		
		return accountResponse;
	}

	private Transaction createTransaction(AccountRequest bankingRequest,String transactionType) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		BankAccount bankAccount = new BankAccount();
		bankAccount.setAccountNumber(bankingRequest.getBankAccountNumber());
		bankAccount.setName(bankingRequest.getCustomerName());
		bankAccount.setBalance(bankingRequest.getInitialBalance());
		bankAccount.setCreatedTime(LocalDateTime.now());
		
		return bankAccount;
	}

	@Override
	public boolean creditTransaction(Integer accountNumber, Double amount) throws InterruptedException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
