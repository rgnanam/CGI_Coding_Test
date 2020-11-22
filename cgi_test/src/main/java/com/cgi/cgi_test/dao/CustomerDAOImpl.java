package com.cgi.cgi_test.dao;

import com.cgi.cgi_test.dto.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomerDAOImpl implements Dao<Customer> {
    private Map<String, Customer> customerMap = new ConcurrentHashMap<>();
    @Override
    public Optional<Customer> get(String id) {
        return Optional.of(customerMap.get(id));
    }

    @Override
    public List<Customer> getAll() {
        return new ArrayList<Customer>(customerMap.values());

    }
    @Override
    public void saveOrUpdate(Customer customer) {
        customerMap.put(customer.getCustomerId(),customer);
    }

}
