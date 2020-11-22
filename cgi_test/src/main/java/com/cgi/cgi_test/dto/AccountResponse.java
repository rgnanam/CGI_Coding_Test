package com.cgi.cgi_test.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountResponse implements Serializable {

	private static final long serialVersionUID = 1281811858823667027L;
	private Integer bankAccountNumber;
	private String customerName;
	private Double balance;

}
