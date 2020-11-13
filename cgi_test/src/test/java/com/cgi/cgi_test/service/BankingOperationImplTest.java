package com.cgi.cgi_test.service;

import com.cgi.cgi_test.cachedb.BankingDBStore;
import com.cgi.cgi_test.common.Constants;
import com.cgi.cgi_test.dto.AccountRequest;
import com.cgi.cgi_test.dto.AccountResponse;
import com.cgi.cgi_test.dto.BankAccount;
import com.cgi.cgi_test.dto.Transaction;
import com.cgi.cgi_test.exception.CGIBankOperationException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
class BankingOperationImplTest {
    @InjectMocks
    BankingOperationImpl bankingOperation = new BankingOperationImpl();
    @Mock
    BankingDBStore bankDBStore;

    @BeforeEach
    void setUp() {
        bankingOperation = new BankingOperationImpl();
        bankDBStore = mock(BankingDBStore.class);
        ReflectionTestUtils.setField(bankingOperation, "bankDBStore", bankDBStore);

        when(bankDBStore.get(anyInt())).thenReturn(createBankAccount());

    }

    private BankAccount createBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(100);
        bankAccount.setName("Gnanasekaran");
        bankAccount.setBalance(1000.0);
        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setTransactionType(Constants.CREDIT);
        transaction.setBalance(1000.0);
        transaction.setAmount(100.0);
        transactionList.add(transaction);
        bankAccount.setTransactionList(transactionList);
        return bankAccount;
    }

    @Test
    void createAccount() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setInitialBalance(1000.0);
        accountRequest.setCustomerName("Gnanasekaran R");
        accountRequest.setBankAccountNumber(100);
        boolean statusFlag = bankingOperation.createAccount(accountRequest);
        Assert.assertTrue(statusFlag);
    }

    @Test
    void getAccount() throws Exception{
        List<AccountResponse> accountResponseList = bankingOperation.getAccount(100);
        Assert.assertTrue(accountResponseList.size()>=1);
    }

    @Test
    void creditTransaction() throws InterruptedException {
        boolean statusFlag = bankingOperation.creditTransaction(100,200.0);
        Assert.assertTrue(statusFlag);
    }

    @Test
    void debitTransaction() throws CGIBankOperationException, InterruptedException {
        boolean statusFlag = bankingOperation.debitTransaction(100,200.0);
        Assert.assertTrue(statusFlag);
    }

    @Test
    void transactionHistory() {
        List<Transaction> transactionList = bankingOperation.transactionHistory(100);
        Assert.assertTrue(transactionList.size()>=1);
    }
}