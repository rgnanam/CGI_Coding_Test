package com.cgi.cgi_test.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
	String transactionId;
	String transactionType;
	Integer accountNumber;
	Double  amount;
	Double  balance;
	LocalDateTime createdTime;
	
}
