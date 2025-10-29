package com.snorlax.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.domain.OrderStatus;
import com.snorlax.modal.Order;
import com.snorlax.modal.Seller;
import com.snorlax.service.OrderService;
import com.snorlax.service.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/orders")
public class SellerOrderController {
	
	private final OrderService orderService;
	private final SellerService sellerService;
	
	@GetMapping
	public ResponseEntity<List<Order>> getAllOrdersHandler(
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		Seller seller = sellerService.getSellerProfile(jwt);
		List<Order> orders = orderService.sellerOrder(seller.getId());
		
		return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
	}
	
	@PatchMapping("/{orderId}/status/{orderStatus}")
	public ResponseEntity<Order> updateOrderStatus(
			@PathVariable Long orderId,
			@PathVariable OrderStatus orderStatus,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		Seller seller = sellerService.getSellerProfile(jwt);
		Order order = orderService.updateOrderStatus(orderId, orderStatus);
		
		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}

}
