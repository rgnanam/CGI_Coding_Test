package com.cgi.cgi_test.common;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class CommonUtility {
    private static int transactionCount=1;
    private static int customerCount=1;
    private static int bankAccountCount=1;

    public static String generateTransactionID(String transactionType){
        StringJoiner tokenJoiner = new StringJoiner("-");
        tokenJoiner.add("CGI");
        tokenJoiner.add("BANK");
        tokenJoiner.add("TRANS");
        tokenJoiner.add(transactionType);
        tokenJoiner.add(String.valueOf(LocalDateTime.now().getYear()));
        tokenJoiner.add(String.valueOf(transactionCount));
        transactionCount++;
        return tokenJoiner.toString();

    }
    public static String generateCustomerID(){
        StringJoiner tokenJoiner = new StringJoiner("-");
        tokenJoiner.add("CGI");
        tokenJoiner.add("BANK");
        tokenJoiner.add("CUST");
        tokenJoiner.add(String.valueOf(LocalDateTime.now().getYear()));
        tokenJoiner.add(String.valueOf(customerCount));
        customerCount++;
        return tokenJoiner.toString();

    }
    public static String generateAccountId(){
        StringJoiner tokenJoiner = new StringJoiner("-");
        tokenJoiner.add("CGI");
        tokenJoiner.add("BANK");
        tokenJoiner.add("ACCT");
        tokenJoiner.add(String.valueOf(LocalDateTime.now().getYear()));
        tokenJoiner.add(String.valueOf(bankAccountCount));
        bankAccountCount++;
        return tokenJoiner.toString();

    }
}
