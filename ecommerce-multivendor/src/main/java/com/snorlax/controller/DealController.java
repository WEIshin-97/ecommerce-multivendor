package com.snorlax.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.modal.Deal;
import com.snorlax.response.ApiResponse;
import com.snorlax.service.DealService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {
	
	private final DealService dealService;
	
	@PostMapping
	public ResponseEntity<Deal> createDealHandler(
			@RequestBody Deal deal) {
		
		Deal newDeal = dealService.createDeal(deal);
		
		return new ResponseEntity<>(newDeal, HttpStatus.CREATED);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Deal> updateDealHandler(
			@PathVariable Long id,
			@RequestBody Deal deal) throws Exception {
		
		Deal updateDeal = dealService.updateDeal(id, deal);
		
		return new ResponseEntity<>(updateDeal, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteDealHandler(
			@PathVariable Long id) throws Exception {
		
		dealService.deleteDeal(id);
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Deal Removed");
		
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

}
