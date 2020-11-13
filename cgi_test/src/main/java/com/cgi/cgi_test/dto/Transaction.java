package com.cgi.cgi_test.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
	String transactionId;
	String transactionType;
	Integer accountNumber;
	Double  amount;
	Double  balance;
	LocalDateTime createdTime;
}
