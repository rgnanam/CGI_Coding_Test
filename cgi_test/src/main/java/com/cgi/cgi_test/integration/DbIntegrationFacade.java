package com.cgi.cgi_test.integration;

import com.cgi.cgi_test.dao.Dao;
import com.cgi.cgi_test.dto.BankAccount;
import com.cgi.cgi_test.dto.Customer;
import com.cgi.cgi_test.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DbIntegrationFacade {


    @Autowired
    Dao<Customer> customerDao;

    @Autowired
    Dao<BankAccount> bankAccountDao;

    @Autowired
    Dao<Transaction> transactionDao;

    public void save(Customer customer,BankAccount bankAccount,Transaction transaction){
        createMapping(customer,bankAccount,transaction);
        transactionDao.saveOrUpdate(transaction);
        bankAccountDao.saveOrUpdate(bankAccount);
        customerDao.saveOrUpdate(customer);

    }
    private void createMapping(Customer customer,BankAccount bankAccount,Transaction transaction){
        bankAccount.addTransaction(transaction.getTransactionId());
        customer.addBankAccount(bankAccount.getBankAccoundId());
    }
    public BankAccount getAccountByNumber(Integer number) {
        return bankAccountDao.getAll().stream().filter(bankAccount -> bankAccount.getAccountNumber()==number.intValue()).findFirst().get();

    }
    public Customer getCustomerByAccountNumber(Integer number) {
        Optional<BankAccount> optionalBankAccount = bankAccountDao.getAll().stream().filter(bankAccount -> bankAccount.getAccountNumber()==number.intValue()).findFirst();
        if(optionalBankAccount.isPresent()) {
            BankAccount bankAccount = optionalBankAccount.get();
            return customerDao.getAll().stream().filter(customer -> customer.getBankAccountIdList().contains(bankAccount.getBankAccoundId())).findFirst().get();
        }else{
            return null;
        }

    }
    public List<BankAccount> getAllAccounts() {
        return bankAccountDao.getAll();
    }
    public void save(BankAccount bankAccount,Transaction transaction){
        createMapping(bankAccount,transaction);
        transactionDao.saveOrUpdate(transaction);
        bankAccountDao.saveOrUpdate(bankAccount);
    }

    private void createMapping(BankAccount bankAccount, Transaction transaction) {
        bankAccount.addTransaction(transaction.getTransactionId());
    }

    public List<Transaction> getAllTransactions(Integer accountNumber){
        return transactionDao.getAll().stream().filter(transaction -> transaction.getAccountNumber()==accountNumber.intValue()).
                sorted(Comparator.comparing(Transaction::getCreatedTime).reversed()).collect(Collectors.toList());
    }

    public BankAccount getBankAccount(Transaction transaction) {

        return bankAccountDao.getAll().stream().filter(bankAccount -> bankAccount.getTransactionIdList().contains(transaction.getTransactionId())).findFirst().get();
    }


}
