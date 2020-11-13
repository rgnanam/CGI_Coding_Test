package com.cgi.cgi_test.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class AccountRequest implements Serializable {

	private static final long serialVersionUID = 1138427459680070403L;
	@NotNull(message="BankAccountNumber is mandatory")
	private Integer bankAccountNumber;
	@NotNull(message="CustomerName is mandatory")
	private String customerName;
	@NotNull(message="InitialBalance is mandatory")
	@Min(value=1000,message="Minimum balance is 1000 required")
	private Double initialBalance;
	
}
