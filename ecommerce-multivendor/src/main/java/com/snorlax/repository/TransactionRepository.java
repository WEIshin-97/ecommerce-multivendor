package com.snorlax.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.modal.Transaction;

public interface TransactionRepository  extends JpaRepository<Transaction, Long>{

	List<Transaction> findBySellerid(Long sellerId);
}
