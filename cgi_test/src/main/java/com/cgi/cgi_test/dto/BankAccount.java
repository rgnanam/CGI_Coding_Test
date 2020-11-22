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

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@ToString
public class BankAccount {

	private String bankAccoundId;
	private Integer accountNumber;
	private Double balance;
	private LocalDateTime createdTime;
	private LocalDateTime modifiedTime;
	private boolean activeFlag;
	private List<String> transactionIdList = new ArrayList<>();
	private Lock lock = new ReentrantLock();
	
	public void addTransaction(String transactionId){
		transactionIdList.add(transactionId);
	}
	
	// It is used for credit transaction
	public Transaction creditTransaction(Double amount) throws InterruptedException{
		log.info("Begin credit Transaction");
		boolean status=false;
		Transaction transaction = new Transaction(CommonUtility.generateTransactionID(Constants.CREDIT),
				Constants.CREDIT,
				accountNumber,
				amount,
				balance,
				LocalDateTime.now(),
				Constants.IN_PROGRESS,
				Constants.HIGHPRIORITY,
				0);
		if(lock.tryLock(5,TimeUnit.SECONDS)){
			try{
				balance = balance +transaction.getAmount();
				transaction.setBalance(balance);
				transaction.setStatus(Constants.SUCCESS);
				status=true;
				log.info(amount+" credited into account"+accountNumber+" successfully");
				log.debug("Transaction("+ transaction +") is created successfully");
			}finally{
				lock.unlock();
				log.info("Lock released");
			}
		}else{
			log.error(amount+" not credited into account"+accountNumber+" due to lock acquired by other thread");
		}
		log.info("End credit Transaction");
		
		return transaction;
	}
	
	// It is used for debit transaction
	public Transaction debitTransaction(Double amount) throws InterruptedException, CGIBankOperationException{
		log.info("Begin debit Transaction");
		Transaction transaction = new Transaction(CommonUtility.generateTransactionID(Constants.DEBIT),
				Constants.DEBIT,
				accountNumber,
				amount,
				balance,
				LocalDateTime.now(),
				Constants.IN_PROGRESS,
				Constants.LOWPRIORITY,
				0);;
		if(lock.tryLock(5,TimeUnit.SECONDS)){
			try{
				if(balance > amount){
					balance = balance-transaction.getAmount();
					transaction.setBalance(balance);
					transaction.setStatus(Constants.SUCCESS);
					log.debug("Transaction("+transaction+") is created successfully");
					log.info(amount+" debited into account"+accountNumber+" successfully");
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
		
		return transaction;
		
	}
	public Transaction updateTransaction(Transaction transaction) throws InterruptedException {
		if(transaction.getStatus().equalsIgnoreCase(Constants.IN_PROGRESS)){
			if(lock.tryLock(5000,TimeUnit.MILLISECONDS))
			{
				try{
					if(transaction.getTransactionType().equalsIgnoreCase(Constants.CREDIT)) {
						balance = balance + transaction.getAmount();
					}else{
						balance = balance - transaction.getAmount();
					}
					transaction.setBalance(balance);
					transaction.setStatus(Constants.SUCCESS);
				}finally {
					lock.unlock();
				}

			}
		}
		return transaction;
	}
}
