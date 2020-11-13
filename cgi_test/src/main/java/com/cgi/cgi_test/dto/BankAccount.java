package com.cgi.cgi_test.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.cgi.cgi_test.common.CommonUtility;
import com.cgi.cgi_test.common.Constants;
import com.cgi.cgi_test.exception.CGIBankOperationException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class BankAccount {
	
	private Integer accountNumber;
	private String name;
	private Double balance;
	private LocalDateTime createdTime;
	private LocalDateTime modifiedTime;
	private List<Transaction> transactionList = new ArrayList<>();
	private Lock lock = new ReentrantLock();
	
	public void addTransaction(Transaction transaction){
		transactionList.add(transaction);
	}
	
	// It is used for credit transaction
	public boolean creditTransaction(Double amount) throws InterruptedException{
		log.info("Begin credit Transaction");
		boolean status=false;
		
		if(lock.tryLock(5,TimeUnit.SECONDS)){
			try{
				balance = balance +amount;
				addTransaction(new Transaction(CommonUtility.generateTransactionID(Constants.CREDIT),
						Constants.CREDIT,
						accountNumber,
						amount,
						balance,
						LocalDateTime.now()));
				status=true;
				log.info(amount+" credited into account"+accountNumber+" successfully");
			}finally{
				lock.unlock();
				log.info("Lock released");
			}
		}else{
			log.error(amount+" not credited into account"+accountNumber+" due to lock acquired by other thread");
		}
		log.info("End credit Transaction");
		
		return status;
	}
	
	// It is used for debit transaction
	public boolean debitTransaction(Double amount) throws InterruptedException, CGIBankOperationException{
		log.info("Begin debit Transaction");
		boolean status=false;
		
		if(lock.tryLock(5,TimeUnit.SECONDS)){
			try{
				if(balance > amount){
					balance = balance-amount;
					addTransaction(new Transaction(CommonUtility.generateTransactionID(Constants.DEBIT),
							Constants.DEBIT,
							accountNumber,
							amount,
							balance,
							LocalDateTime.now()));
					status=true;
					log.debug(amount+" debited into account"+accountNumber+" successfully");
				}else{
					String errorMessage =amount+" not debited into account"+accountNumber+" due to insufficient balance";
					log.error(errorMessage);
					throw new CGIBankOperationException(Constants.CGIE301, errorMessage);
				}
			}finally{
				lock.unlock();
				log.info("Lock released");
			}
		}else{
			log.error(amount+" not debited into account"+accountNumber+" due to lock acquired by other thread");
		}
		log.info("End debit Transaction");
		
		return status;
		
	}
}
