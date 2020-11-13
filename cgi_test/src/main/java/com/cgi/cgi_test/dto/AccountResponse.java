package com.cgi.cgi_test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse implements Serializable {

	private static final long serialVersionUID = 1281811858823667027L;
	private Integer bankAccountNumber;
	private String customerName;
	private Double balance;

}
