package com.snorlax.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.config.JwtProvider;
import com.snorlax.domain.AccountStatus;
import com.snorlax.exception.SellerException;
import com.snorlax.modal.Seller;
import com.snorlax.modal.SellerReport;
import com.snorlax.service.SellerReportService;
import com.snorlax.service.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
	
	private final SellerService sellerService;
	private final SellerReportService sellerReportService;
	

	@GetMapping("/{id}")
	public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException{
		
		Seller seller = sellerService.getSellerById(id);
		
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception{
		
		Seller seller = sellerService.getSellerProfile(jwt);
		
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}
	
	@GetMapping("/report")
	public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt) throws Exception{
		
		Seller seller = sellerService.getSellerProfile(jwt);
		SellerReport report = sellerReportService.getSellerReport(seller);
		
		return new ResponseEntity<>(report, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus status) throws Exception{
		
		List<Seller> sellers = sellerService.getAllSellers(status);
		
		return new ResponseEntity<>(sellers, HttpStatus.OK);
	}
	
	@PatchMapping
	public ResponseEntity<Seller> updateSeller(
			@RequestHeader("Authorization") String jwt,
			@RequestBody Seller seller) throws Exception{
		
		Seller profile = sellerService.getSellerProfile(jwt);
		Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
		
		return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception{
		
		sellerService.deleteSeller(id);
		return ResponseEntity.noContent().build();
	}
	

}
