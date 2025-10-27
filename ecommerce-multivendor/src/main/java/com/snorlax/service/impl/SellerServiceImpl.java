package com.snorlax.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snorlax.config.JwtProvider;
import com.snorlax.domain.AccountStatus;
import com.snorlax.exception.SellerException;
import com.snorlax.modal.Seller;
import com.snorlax.repository.SellerRepository;
import com.snorlax.service.SellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService{
	
	private final SellerRepository sellerRepository;
	private final JwtProvider jwtProvider;
	

	@Override
	public Seller getSellerProfile(String jwt) throws Exception {
		
		String email = jwtProvider.getEmailFromJwtToken(jwt);
		
		return this.getSellerByEmail(email);
	}

	@Override
	public Seller getSellerById(Long id) throws SellerException {
		
		return sellerRepository.findById(id).orElseThrow(() -> new SellerException("Seller not found with id " + id));
	}

	@Override
	public Seller getSellerByEmail(String email) throws Exception {
		
		Seller seller = sellerRepository.findByEmail(email);
		if(seller == null) {
			throw new Exception("seller not found by email "+ email);
		}
		
		return seller;
	}

	@Override
	public List<Seller> getAllSellers(AccountStatus status) {
		return sellerRepository.findByAccountStatus(status);
	}

	@Override
	public Seller updateSeller(Long id, Seller seller) throws Exception {
		
		Seller existSeller = this.getSellerById(id);
		
		if(seller.getFullName() != null) {
			existSeller.setFullName(seller.getFullName());
		}
		
		if(seller.getMobile() != null) {
			existSeller.setMobile(seller.getMobile());
		}
		
		if(seller.getEmail() != null) {
			existSeller.setEmail(seller.getEmail());
		}
		
		if(seller.getBusinessDetails() != null 
				&& seller.getBusinessDetails().getBusinessName() != null) 
		{
			existSeller.getBusinessDetails().setBusinessName(
					seller.getBusinessDetails().getBusinessName()
			);
		}
		
		if(seller.getBankDetails() != null
				&& seller.getBankDetails().getAccountHolderNumber() != null
				&& seller.getBankDetails().getIfscCode() != null
				&& seller.getBankDetails().getAccountNumber() != null
		) {
			existSeller.getBankDetails().setAccountHolderNumber(
					seller.getBankDetails().getAccountHolderNumber()
			);
			
			existSeller.getBankDetails().setIfscCode(
					seller.getBankDetails().getIfscCode()
			);
			
			existSeller.getBankDetails().setAccountNumber(
					seller.getBankDetails().getAccountNumber()
			);
		}
		
		if(seller.getPickupAddress() != null
				&& seller.getPickupAddress().getAddress() != null
				&& seller.getPickupAddress().getMobile() != null
				&& seller.getPickupAddress().getCity() != null
				&& seller.getPickupAddress().getState() != null
				&& seller.getPickupAddress().getPinCode() != null
		) {
			existSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
			existSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
			existSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
			existSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
			existSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
		}
		
		if(seller.getGSTIN() != null) {
			existSeller.setGSTIN(seller.getGSTIN());
		}
		
		return sellerRepository.save(existSeller);
	}

	@Override
	public void deleteSeller(Long id) throws Exception {
		Seller seller = this.getSellerById(id);
		sellerRepository.delete(seller);
		
	}

	@Override
	public Seller verifyEmail(String email, String otp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
		Seller seller = this.getSellerById(sellerId);
		seller.setAccountStatus(status);
		
		return sellerRepository.save(seller);
	}

}
