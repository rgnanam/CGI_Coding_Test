package com.cgi.cgi_test.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AccountResponse implements Serializable {

	private static final long serialVersionUID = 1281811858823667027L;
	private Integer bankAccountNumber;
	private String customerName;
	private Double balance;

}
