package com.cgi.cgi_test.queue;

import com.cgi.cgi_test.dto.Transaction;
import com.cgi.cgi_test.service.BankingOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.*;

@Service
public class TransactionWaitingQueue {
    private static BlockingQueue<Transaction> transactionQueue = new PriorityBlockingQueue<>();
    private static ExecutorService executorService;
    private static TransactionConsumer transactionConsumer;
    @Autowired
    private BankingOperations bankingOperations;

    public TransactionWaitingQueue(){
        executorService = Executors.newWorkStealingPool();
        transactionConsumer= new TransactionConsumer(this,bankingOperations);
        executorService.submit(transactionConsumer);
    }

    public void add(Transaction transaction){
        transactionQueue.add(transaction);
    }
    public Transaction poll() throws InterruptedException {
        return transactionQueue.poll();
    }
    @PreDestroy
    public void destroy(){
        transactionConsumer.setStopFlag(true);
        executorService.shutdown();
    }

}
