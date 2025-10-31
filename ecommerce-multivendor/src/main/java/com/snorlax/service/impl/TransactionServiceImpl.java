package com.snorlax.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snorlax.modal.Order;
import com.snorlax.modal.Seller;
import com.snorlax.modal.Transaction;
import com.snorlax.repository.SellerRepository;
import com.snorlax.repository.TransactionRepository;
import com.snorlax.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
	
	private final TransactionRepository transactionRepository;
	private final SellerRepository sellerRepository;
	
	
	@Override
	public Transaction createTransaction(Order order) {
		
		Seller seller = sellerRepository.findById(order.getSellerId()).get();
		
		Transaction transaction = new Transaction();
		transaction.setSeller(seller);
		transaction.setCustomer(order.getUser());
		transaction.setOrder(order);
		
		return transactionRepository.save(transaction);
	}

	@Override
	public List<Transaction> getTransactionBySellerId(Long sellerId) {
		
		return transactionRepository.findBySellerid(sellerId);
	}

	@Override
	public List<Transaction> getAllTransactions() {
		
		return transactionRepository.findAll();
	}

}
