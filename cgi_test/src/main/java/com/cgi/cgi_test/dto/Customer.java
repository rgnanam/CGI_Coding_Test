package com.cgi.cgi_test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@ToString
public class Customer {
    private String customerId;
    private String name;
    private String panNumber;
    private int aadharNumber;
    List<String> bankAccountIdList = new ArrayList<>();

    public void addBankAccount(String bankAccountId){
        bankAccountIdList.add(bankAccountId);
    }
    //etc
}
