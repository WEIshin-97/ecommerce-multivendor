package com.snorlax.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.domain.PaymentMethod;
import com.snorlax.modal.Address;
import com.snorlax.modal.Cart;
import com.snorlax.modal.Order;
import com.snorlax.modal.OrderItem;
import com.snorlax.modal.PaymentOrder;
import com.snorlax.modal.Seller;
import com.snorlax.modal.SellerReport;
import com.snorlax.modal.User;
import com.snorlax.response.PaymentLinkResponse;
import com.snorlax.service.CartService;
import com.snorlax.service.OrderService;
import com.snorlax.service.PaymentService;
import com.snorlax.service.SellerReportService;
import com.snorlax.service.SellerService;
import com.snorlax.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
	
	private final OrderService orderService;
	private final UserService userService;
	private final CartService cartService;
	private final SellerService sellerService;
	private final SellerReportService sellerReportService;
	private final PaymentService paymentService;
	
	@PostMapping
	public ResponseEntity<PaymentLinkResponse> createOrderHandler(
			@RequestBody Address shippingAddress,
			@RequestParam PaymentMethod paymentMethod,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		Cart cart = cartService.findUserCart(user);
		Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);
		
		
		// create payment order
		PaymentOrder paymentOrder = paymentService.createOrder(user, orders);
		
		PaymentLinkResponse res = new PaymentLinkResponse();
		
		if(paymentMethod.equals(PaymentMethod.STRIPE)) {
			String paymentUrl = paymentService.createStripePaymentLink(user, 
					paymentOrder.getAmount(), 
					paymentOrder.getId());
		
			res.setPayment_link_url(paymentUrl);
		}
		
		return new ResponseEntity<>(res, HttpStatus.CREATED);
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<Order>> usersOrderHistoryHandler(
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		List<Order> orders = orderService.userOrderHistory(user.getId());
		
		return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(
			@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		Order order = orderService.findOrderById(orderId);
		
		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/item/{orderItemId}")
	public ResponseEntity<OrderItem> getOrderItemById(
			@PathVariable Long orderItemId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		OrderItem orderItem = orderService.getOrderItemById(orderItemId);
		
		return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Order> cancelOrder(
			@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		Order order = orderService.cancelOrder(orderId, user);
		
		Seller seller = sellerService.getSellerById(order.getSellerId());
		SellerReport report = sellerReportService.getSellerReport(seller);
		
		report.setCancaledOrder(report.getCancaledOrder() + 1);
		report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
		sellerReportService.updateSellerReport(report);
		
		return ResponseEntity.ok(order);
	}

}
