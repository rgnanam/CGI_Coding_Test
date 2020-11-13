package com.cgi.cgi_test.api;
import com.cgi.cgi_test.CgiTestApplication;
import com.cgi.cgi_test.common.Constants;
import com.cgi.cgi_test.dto.AccountRequest;
import com.cgi.cgi_test.dto.AccountResponse;
import com.cgi.cgi_test.dto.Transaction;
import com.cgi.cgi_test.service.BankingOperation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(classes = CgiTestApplication.class)
    @WebMvcTest(BankingOperationController.class)
    class BankingOperationControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private BankingOperation bankingOperation;

        @BeforeEach
        public void setUp() {
        }

        @Test
        void createAccountWithHappyPath() throws Exception {
            AccountRequest accountRequest = new AccountRequest();
            accountRequest.setBankAccountNumber(100);
            accountRequest.setCustomerName("Gnanasekaran Raja");
            accountRequest.setInitialBalance(1000.0);
            ObjectMapper objectMapper = new ObjectMapper();
            String request =objectMapper.writeValueAsString( accountRequest);
            when(bankingOperation.createAccount(any()))
                    .thenReturn(Boolean.TRUE);
            this.mockMvc.perform(post("/rest/api/v1/bank/account-management/managed-accounts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(request))
                    .andDo(print()).andExpect(status().isOk()).andExpect(content()
                    .string(containsString("successfully")));
        }
        @Test
        void createAccountWithOutAccountNumber() throws Exception {
            AccountRequest accountRequest = new AccountRequest();
            //accountRequest.setBankAccountNumber(100);
            accountRequest.setCustomerName("Gnanasekaran Raja");
            accountRequest.setInitialBalance(1000.0);
            ObjectMapper objectMapper = new ObjectMapper();
            String request =objectMapper.writeValueAsString( accountRequest);
            when(bankingOperation.createAccount(any()))
                    .thenReturn(Boolean.TRUE);
            this.mockMvc.perform(post("/rest/api/v1/bank/account-management/managed-accounts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(request))
                    .andDo(print()).andExpect(status().isBadRequest()).andExpect(content()
                    .string(containsString(Constants.VALIDATION_ERRORS)));
        }

        @Test
        public void createAccountWithOutCustomerName() throws Exception {
            AccountRequest accountRequest = new AccountRequest();
            accountRequest.setBankAccountNumber(100);
            //accountRequest.setCustomerName("Gnanasekaran Raja");
            accountRequest.setInitialBalance(1000.0);
            ObjectMapper objectMapper = new ObjectMapper();
            String request =objectMapper.writeValueAsString( accountRequest);
            when(bankingOperation.createAccount(any()))
                    .thenReturn(Boolean.TRUE);
            this.mockMvc.perform(post("/rest/api/v1/bank/account-management/managed-accounts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(request))
                    .andDo(print()).andExpect(status().isBadRequest()).andExpect(content()
                    .string(containsString(Constants.VALIDATION_ERRORS)));
        }


        @Test
        public void getAccount() throws Exception {

            when(bankingOperation.getAccount(any()))
                    .thenReturn(createAccountResponse());
            ObjectMapper objectMapper = new ObjectMapper();
            MvcResult result= this.mockMvc.perform(get("/rest/api/v1/bank/account-management/managed-accounts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).param("id","100"))
                    .andDo(print()).andExpect(status().isOk()).andReturn();
            List<AccountResponse> accountResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<AccountResponse>>(){});
            Assert.assertEquals(1,accountResponseList.size(),0);
            Assert.assertEquals(100,accountResponseList.get(0).getBankAccountNumber(),0);

    }

        @Test
        public void getAllAccount() throws Exception {

            when(bankingOperation.getAccount(any()))
                    .thenReturn(createAccountResponseListWithMultipleAccounts());
            ObjectMapper objectMapper = new ObjectMapper();
            MvcResult result= this.mockMvc.perform(get("/rest/api/v1/bank/account-management/managed-accounts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andDo(print()).andExpect(status().isOk()).andReturn();
            List<AccountResponse> accountResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<AccountResponse>>(){});
            Assert.assertTrue(accountResponseList.size()>1);

        }

        private List<AccountResponse> createAccountResponseListWithMultipleAccounts() {
            List<AccountResponse> listAccountResponse = new ArrayList<>();
            AccountResponse accountResponse1 = new AccountResponse();
            accountResponse1.setBankAccountNumber(100);
            accountResponse1.setCustomerName("gnanasekaran Raja");
            accountResponse1.setBalance(1000.0);
            listAccountResponse.add(accountResponse1);

            AccountResponse accountResponse2 = new AccountResponse();
            accountResponse2.setBankAccountNumber(101);
            accountResponse2.setCustomerName("gnanasekaran Raja");
            accountResponse2.setBalance(1000.0);
            listAccountResponse.add(accountResponse2);

            return listAccountResponse;

        }

        private List<AccountResponse> createAccountResponse() {
            List<AccountResponse> listAccountResponse = new ArrayList<>();
            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setBankAccountNumber(100);
            accountResponse.setCustomerName("gnanasekaran Raja");
            accountResponse.setBalance(1000.0);
            listAccountResponse.add(accountResponse);
            return listAccountResponse;
        }

        @Test
   public void createCreditTransaction() throws Exception {

            when(bankingOperation.creditTransaction(any(),anyDouble()))
                    .thenReturn(Boolean.TRUE);
            this.mockMvc.perform(post("/rest/api/v1/bank/account-management/managed-accounts/{id}/credit/{amount}",100,100).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andDo(print()).andExpect(status().isOk()).andExpect(content()
                    .string(containsString("successfully")));
    }

    @Test
    public void createDebitTransaction() throws Exception {
        when(bankingOperation.debitTransaction(any(),anyDouble()))
                .thenReturn(Boolean.TRUE);
        this.mockMvc.perform(post("/rest/api/v1/bank/account-management/managed-accounts/{id}/debit/{amount}",100,100).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andExpect(content()
                .string(containsString("successfully")));
    }

    @Test
    public void getALLTransactions() throws Exception{
        when(bankingOperation.transactionHistory(any()))
                .thenReturn(createTransactionList());
        MvcResult result = this.mockMvc.perform(get("/rest/api/v1/bank/account-management/managed-accounts/{id}/transactions",100).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Transaction> transactionList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Transaction>>(){});
        Assert.assertTrue(transactionList.size()>=1);

    }

        private List<Transaction> createTransactionList() {
            List<Transaction> transactionList = new ArrayList<>();
            Transaction transaction = new Transaction();
            transaction.setAccountNumber(100);
            transaction.setBalance(1000.0);
            transaction.setTransactionType(Constants.CREDIT);
            transactionList.add(transaction);
            return transactionList;
        }
    }