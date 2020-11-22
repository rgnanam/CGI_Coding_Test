package com.cgi.cgi_test.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cgi.cgi_test.queue.TransactionWaitingQueue;
import com.cgi.cgi_test.common.CommonUtility;
import com.cgi.cgi_test.dto.*;
import com.cgi.cgi_test.integration.DbIntegrationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgi.cgi_test.common.Constants;
import com.cgi.cgi_test.exception.CGIBankOperationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankingOperationsImpl implements BankingOperations {
	
	

	@Autowired
	DbIntegrationFacade dbIntegrationFacade;

	@Autowired
	TransactionWaitingQueue transactionWaitingQueue;


	@Override
	public boolean createAccount(AccountRequest bankingRequest) {
		log.info("Begin createAccount");
		boolean flag= false;
		try{
			Customer customer = createCustomer(bankingRequest);
			BankAccount bankAccount = createBankAccount(bankingRequest);
			Transaction transaction = createTransaction(bankingRequest,Constants.CREDIT);
			dbIntegrationFacade.save(customer,bankAccount,transaction);
			log.debug("Transaction Object is created ->"+transaction);
			log.debug("bankAccount Object is created ->"+bankAccount);
			log.debug("Customer Object is created ->"+customer);
			log.debug("Account# "+bankAccount.getAccountNumber()+" Bank account is created successfully");
			flag=true;
		}catch(Exception e){
			log.error(e.getMessage());
			// implement Email notification
		}
		log.info("End createAccount");
		return flag;
		
	}

	private Customer createCustomer(AccountRequest bankingRequest) {
		Customer customer = new Customer();
		customer.setCustomerId(CommonUtility.generateCustomerID());
		customer.setName(bankingRequest.getCustomerName());
		customer.setAadharNumber(0000);
		customer.setPanNumber("XXXXXXX");
		return customer;
	}

	@Override
	public List<AccountResponse> getAccount(Integer accountNumber) throws CGIBankOperationException {
		log.info("Begin getAccount");
		List<AccountResponse> listAccountResponse = new ArrayList<>();
		if(accountNumber != null){
			BankAccount bankAccount = dbIntegrationFacade.getAccountByNumber(accountNumber);
			if(bankAccount != null){
				listAccountResponse.add(createAccountResponse(bankAccount));
				return listAccountResponse;
			}else{
				String errorMsg =accountNumber+" account is not found in CGI service";
				log.debug(errorMsg);
				throw new CGIBankOperationException(Constants.CGIE300,errorMsg);
			}
		}else{
			listAccountResponse = dbIntegrationFacade.getAllAccounts().stream().map(account->createAccountResponse(account)).collect(Collectors.toList());
		}
		log.info("End getAccount");
		return listAccountResponse;
		
	}

	@Override
	public void updateTransaction(Transaction transaction) {
		BankAccount bankAccount = dbIntegrationFacade.getBankAccount(transaction);
		if(bankAccount !=null){
			try {
				Transaction updatedTransaction = bankAccount.updateTransaction(transaction);
				if(updatedTransaction.getTransactionType().equalsIgnoreCase(Constants.IN_PROGRESS)){
					if(transaction.getRetryCounter() <3){
					updatedTransaction.incrementRetryCounter();
					transactionWaitingQueue.add(transaction);
					}
					else {
						updatedTransaction.setStatus(Constants.FAILED);
					}
				}
				dbIntegrationFacade.save(bankAccount,transaction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private AccountResponse createAccountResponse(BankAccount bankAccount) {
		AccountResponse accountResponse= new AccountResponse();
		accountResponse.setBankAccountNumber(bankAccount.getAccountNumber());
		accountResponse.setCustomerName(dbIntegrationFacade.getCustomerByAccountNumber(bankAccount.getAccountNumber()).getName());
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
		transaction.setStatus(Constants.SUCCESS);
		
		return transaction;
	}

	private BankAccount createBankAccount(AccountRequest bankingRequest) {
		BankAccount bankAccount = new BankAccount();
		bankAccount.setBankAccoundId(CommonUtility.generateAccountId());
		bankAccount.setAccountNumber(bankingRequest.getBankAccountNumber());
		bankAccount.setBalance(bankingRequest.getInitialBalance());
		bankAccount.setCreatedTime(LocalDateTime.now());
		bankAccount.setActiveFlag(true);
		
		return bankAccount;
	}

	@Override
	public Transaction creditTransaction(Integer accountNumber, Double amount) throws InterruptedException {
		log.info("Begin creditTransaction");
		BankAccount bankAccount = dbIntegrationFacade.getAccountByNumber(accountNumber);
		Transaction transaction=null;
		if(bankAccount !=null){
			transaction =bankAccount.creditTransaction(amount);
			dbIntegrationFacade.save(bankAccount,transaction);
			if(transaction.getStatus().equalsIgnoreCase(Constants.IN_PROGRESS)){
				transaction.incrementRetryCounter();
				transactionWaitingQueue.add(transaction);
			}
		}else{
			log.error(accountNumber+" is invalid account number");
		}
		log.info("End creditTransaction");
		return transaction;
	}

	@Override
	public Transaction debitTransaction(Integer accountNumber, Double amount) throws InterruptedException, CGIBankOperationException {
		log.info("Begin debitTransaction");
		BankAccount bankAccount = dbIntegrationFacade.getAccountByNumber(accountNumber);
		Transaction transaction=null;
		if(bankAccount !=null){
			transaction=bankAccount.debitTransaction(amount);
			dbIntegrationFacade.save(bankAccount,transaction);
			if(transaction.getStatus().equalsIgnoreCase(Constants.IN_PROGRESS)){
				transaction.incrementRetryCounter();
				transactionWaitingQueue.add(transaction);
			}
		}else{
			log.error(accountNumber+" is invalid account number");
		}
		log.info("End debitTransaction");
		return transaction;

	}

	@Override
	public List<Transaction> transactionHistory(Integer accountNumber) {
		log.info("Begin transactionHistory");
		List<Transaction> allTransactions = dbIntegrationFacade.getAllTransactions(accountNumber);
		log.info("End transactionHistory");
		return allTransactions;
		
	}

}
