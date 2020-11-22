package com.cgi.cgi_test.api;

import java.util.List;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.cgi_test.common.Constants;
import com.cgi.cgi_test.dto.AccountRequest;
import com.cgi.cgi_test.dto.AccountResponse;
import com.cgi.cgi_test.dto.Transaction;
import com.cgi.cgi_test.exception.CGIBankOperationException;
import com.cgi.cgi_test.service.BankingOperations;

import static com.cgi.cgi_test.common.Constants.CGIE302;

@RestController
@RequestMapping("/rest/api/v1/bank/account-management")
@Validated
@Slf4j
public class BankingOperationController {
	
	@Autowired
	BankingOperations bankingOperations;
	
	@PostMapping(value="/managed-accounts",consumes={MediaType.APPLICATION_JSON_VALUE},produces={MediaType.APPLICATION_JSON_VALUE})
	public String createAccount(@Validated @RequestBody AccountRequest bankingRequest){
		log.debug("AccountRequest ->"+bankingRequest);
		return bankingOperations.createAccount(bankingRequest)?String.format(Constants.ACCOUNT_CREATED_MSG,bankingRequest.getBankAccountNumber()):Constants.ACCOUNT_NOTCREATED_MSG;
		
	}
	@GetMapping(value={"/managed-accounts/{id}","/managed-accounts"},produces={MediaType.APPLICATION_JSON_VALUE})
	public List<AccountResponse> getAccount(@PathVariable(required = false) Integer id) throws CGIBankOperationException{
		
		return bankingOperations.getAccount(id);
		
	}
	
	/*@PostMapping(value="/managed-accounts/{id}/credit/{amount}",produces={MediaType.APPLICATION_JSON_VALUE})
	public String createCreditTransaction(@PathVariable Integer id,@PathVariable Double amount) throws CGIBankOperationException, InterruptedException{
		
		return bankingOperations.creditTransaction(id, amount)?String.format(Constants.CREDIT_TRANSACTION_CREATED_MSG,amount,id):Constants.CREDIT_TRANSACTION_FAILED_MSG;
		
	}*/
	
	@PostMapping(value="/managed-accounts/{id}/transaction/{type}/{amount}",produces={MediaType.APPLICATION_JSON_VALUE})
	public Transaction createTransaction(@PathVariable Integer id,@PathVariable String type,@PathVariable Double amount) throws CGIBankOperationException, InterruptedException{
		log.debug("create transaction for Account# "+id+" TransactionType = "+type+" Amount = "+amount);
		if(type.equalsIgnoreCase(Constants.DEBIT)) {
			return bankingOperations.debitTransaction(id, amount);
		}else if(type.equalsIgnoreCase(Constants.CREDIT)){
			return bankingOperations.creditTransaction(id, amount);
		}else{
			String errorMessage ="Unsupported transaction type "+type;
			log.error(errorMessage);
			throw new CGIBankOperationException(CGIE302,errorMessage);
		}
		
	}
	
	@GetMapping(value="/managed-accounts/{id}/transactions",produces={MediaType.APPLICATION_JSON_VALUE})
	public List<Transaction> getALLTransactions(@PathVariable(required = false) Integer id) throws CGIBankOperationException{
		log.debug("GetALLTransactions Account# "+id);
		return bankingOperations.transactionHistory(id);
		
	}
	
	
	

}
