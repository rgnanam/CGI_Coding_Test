package com.cgi.cgi_test.dto;

import java.time.LocalDateTime;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction implements Comparable<Transaction> {
	private String transactionId;
	private String transactionType;
	private Integer accountNumber;
	private Double  amount;
	private Double  balance;
	private LocalDateTime createdTime;
	private String status;
	@JsonIgnore
	private Integer priority;
	@JsonIgnore
	private int retryCounter;

	@Override
	public int compareTo(Transaction transaction) {
		 return priority.compareTo(transaction.getPriority());
	}
	public void incrementRetryCounter(){
		retryCounter=retryCounter+1;
	}
}
