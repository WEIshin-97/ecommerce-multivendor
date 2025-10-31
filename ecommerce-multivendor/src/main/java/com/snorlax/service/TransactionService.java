package com.snorlax.service;

import java.util.List;

import com.snorlax.modal.Order;
import com.snorlax.modal.Transaction;

public interface TransactionService {
	
	Transaction createTransaction(Order order);
	
	List<Transaction> getTransactionBySellerId(Long sellerId);
	
	List<Transaction> getAllTransactions();

}
