package com.cgi.cgi_test.dao;

import com.cgi.cgi_test.dto.BankAccount;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BankAccountDAOImpl implements Dao<BankAccount> {
    private Map<String, BankAccount> bankAccountMap = new ConcurrentHashMap<>();
    @Override
    public Optional<BankAccount> get(String id) {
        return Optional.of(bankAccountMap.get(id));
    }

    @Override
    public List<BankAccount> getAll() {
        return new ArrayList<BankAccount>(bankAccountMap.values());
    }

    @Override
    public void saveOrUpdate(BankAccount bankAccount) {
        bankAccountMap.put(bankAccount.getBankAccoundId(),bankAccount);
    }
}
