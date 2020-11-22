package com.cgi.cgi_test.queue;

import com.cgi.cgi_test.dto.Transaction;
import com.cgi.cgi_test.service.BankingOperations;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
public class TransactionConsumer implements Runnable {
    private boolean stopFlag;
    private TransactionWaitingQueue waitingQueue;
    private BankingOperations bankingOperations;

    public TransactionConsumer(TransactionWaitingQueue  waitingQueue,BankingOperations bankingOperations){
        this.waitingQueue = waitingQueue;
        this.bankingOperations = bankingOperations;
    }
    @SneakyThrows
    @Override
    public void run() {

        while(!stopFlag){
            Transaction transaction = waitingQueue.poll();
            if(transaction != null){
                bankingOperations.updateTransaction(transaction);
            }

        }

    }
}
