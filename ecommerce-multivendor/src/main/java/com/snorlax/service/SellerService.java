package com.snorlax.service;

import java.util.List;

import com.snorlax.domain.AccountStatus;
import com.snorlax.exception.SellerException;
import com.snorlax.modal.Seller;

public interface SellerService {
	
	Seller getSellerProfile(String jwt) throws Exception;
	Seller getSellerById(Long id) throws SellerException;
	
	Seller getSellerByEmail(String email) throws Exception;
	List<Seller> getAllSellers(AccountStatus status);
	Seller updateSeller(Long id, Seller seller) throws Exception;
	
	void deleteSeller(Long id) throws Exception;
	Seller verifyEmail(String email, String otp);
	Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception;
	
}
