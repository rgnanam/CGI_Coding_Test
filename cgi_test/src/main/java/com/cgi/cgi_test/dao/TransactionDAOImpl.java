package com.cgi.cgi_test.dao;

import com.cgi.cgi_test.dto.Customer;
import com.cgi.cgi_test.dto.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TransactionDAOImpl implements Dao<Transaction> {
    private Map<String, Transaction> transactionMap = new ConcurrentHashMap<>();
    @Override
    public Optional<Transaction> get(String id) {
        return Optional.of(transactionMap.get(id));
    }

    @Override
    public List<Transaction> getAll() {
        return new ArrayList<Transaction>(transactionMap.values());
    }

    @Override
    public void saveOrUpdate(Transaction transaction) {
        transactionMap.put(transaction.getTransactionId(),transaction);
    }
}
