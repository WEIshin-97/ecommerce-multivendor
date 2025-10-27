package com.snorlax.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.domain.AccountStatus;
import com.snorlax.modal.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long>{
	
	Seller findByEmail(String email);
	
	List<Seller> findByAccountStatus(AccountStatus status);

}
