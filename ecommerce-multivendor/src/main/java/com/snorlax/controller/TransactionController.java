package com.snorlax.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.modal.Seller;
import com.snorlax.modal.Transaction;
import com.snorlax.service.SellerService;
import com.snorlax.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
	
	private final TransactionService transactionService;
	private final SellerService sellerService;
	
	@GetMapping("/seller")
	public ResponseEntity<List<Transaction>> getTransactionBySeller(
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		Seller seller = sellerService.getSellerProfile(jwt);
		
		List<Transaction> transactions = transactionService.getTransactionBySellerId(seller.getId());
		
		return ResponseEntity.ok(transactions);	
		
	}
	
	@GetMapping
	public ResponseEntity<List<Transaction>> getAllTransactions(){
		
		List<Transaction> transactions = transactionService.getAllTransactions();
		
		return ResponseEntity.ok(transactions);	
		
	}

}
