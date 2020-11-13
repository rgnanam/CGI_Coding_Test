package com.cgi.cgi_test.common;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class CommonUtility {
    private static int count=1;
    public static String generateTransactionID(String transactionType){
        StringJoiner tokenJoiner = new StringJoiner("/");
        tokenJoiner.add("CGI");
        tokenJoiner.add("BANK");
        tokenJoiner.add("TRANS");
        tokenJoiner.add(transactionType);
        tokenJoiner.add(String.valueOf(LocalDateTime.now().getYear()));
        tokenJoiner.add(String.valueOf(count));
        count++;
        return tokenJoiner.toString();

    }
}
