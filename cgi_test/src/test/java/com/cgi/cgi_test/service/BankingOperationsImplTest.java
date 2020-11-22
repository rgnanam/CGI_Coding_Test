package com.cgi.cgi_test.service;

import com.cgi.cgi_test.cachedb.BankingDBStore;
import com.cgi.cgi_test.common.CommonUtility;
import com.cgi.cgi_test.common.Constants;
import com.cgi.cgi_test.dto.*;
import com.cgi.cgi_test.exception.CGIBankOperationException;
import com.cgi.cgi_test.integration.DbIntegrationFacade;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
class BankingOperationsImplTest {

    @InjectMocks
    BankingOperationsImpl bankingOperation;
    @Mock
    DbIntegrationFacade dbIntegrationFacade;

    @BeforeEach
    void setUp() {
        bankingOperation = new BankingOperationsImpl();
        dbIntegrationFacade = mock(DbIntegrationFacade.class);
        ReflectionTestUtils.setField(bankingOperation, "dbIntegrationFacade", dbIntegrationFacade);

        when(dbIntegrationFacade.getAccountByNumber(anyInt())).thenReturn(createBankAccount());
        when(dbIntegrationFacade.getAllTransactions(anyInt())).thenReturn(createTransactionList());
        when(dbIntegrationFacade.getCustomerByAccountNumber(anyInt())).thenReturn(createCustomer());

    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(CommonUtility.generateCustomerID());
        customer.setName("Gnanasekaran Raja");
        customer.setPanNumber("XXXXXX");
        return customer;
    }

    private List<Transaction> createTransactionList() {

        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setTransactionType(Constants.CREDIT);
        transaction.setBalance(1000.0);
        transaction.setAmount(100.0);
        transactionList.add(transaction);
        return transactionList;
    }

    private BankAccount createBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(100);
        bankAccount.setBalance(1000.0);
        bankAccount.addTransaction(CommonUtility.generateTransactionID(Constants.CREDIT));
        return bankAccount;
    }

    @Test
    public void testCreateAccountWithHappyPath() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setInitialBalance(1000.0);
        accountRequest.setCustomerName("Gnanasekaran R");
        accountRequest.setBankAccountNumber(100);
        boolean statusFlag = bankingOperation.createAccount(accountRequest);
        Assert.assertTrue(statusFlag);
    }

    @Test
    public void testGetAccountWithHappyPath() throws Exception{
        List<AccountResponse> accountResponseList = bankingOperation.getAccount(100);
        Assert.assertTrue(accountResponseList.size()>=1);
    }

    @Test
    public void testCreditTransactionWithHappyPath() throws InterruptedException {
        Transaction transaction = bankingOperation.creditTransaction(100,200.0);
        Assert.assertNotNull(transaction);
    }

    @Test
    public void testDebitTransactionWithHappyPath() throws CGIBankOperationException, InterruptedException {
        Transaction transaction = bankingOperation.debitTransaction(100,200.0);
        Assert.assertNotNull(transaction);
    }

    @Test
    void testTransactionHistoryWithHappyPath() {
        List<Transaction> transactionList = bankingOperation.transactionHistory(100);
        Assert.assertTrue(transactionList.size()>=1);
    }
}