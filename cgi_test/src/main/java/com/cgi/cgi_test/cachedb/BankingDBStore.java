package com.cgi.cgi_test.cachedb;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cgi.cgi_test.dto.BankAccount;

@Service
@Qualifier("BankDBStore")
public class BankingDBStore {
	
	private Map<Integer,BankAccount> bankDbStore = new ConcurrentHashMap<>(); // Concurrent hashmap is used to read without lock and write with lock handled effectivelly
	
	public void createOrUpdate(BankAccount bankAccount){
		bankDbStore.put(bankAccount.getAccountNumber(), bankAccount);
	}
	public BankAccount get(Integer accountNumber){
		return bankDbStore.get(accountNumber);
	}
	public Collection<BankAccount> getAll(){
		return bankDbStore.values();
	}

}
