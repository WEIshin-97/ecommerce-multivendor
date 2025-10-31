package com.snorlax.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.modal.Order;
import com.snorlax.modal.PaymentOrder;
import com.snorlax.modal.Seller;
import com.snorlax.modal.SellerReport;
import com.snorlax.modal.User;
import com.snorlax.response.ApiResponse;
import com.snorlax.response.PaymentLinkResponse;
import com.snorlax.service.PaymentService;
import com.snorlax.service.SellerReportService;
import com.snorlax.service.SellerService;
import com.snorlax.service.TransactionService;
import com.snorlax.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
	
	private final PaymentService paymentService;
	private final UserService userService;
	private final SellerService sellerService;
	private final SellerReportService sellerReportService;
	private final TransactionService transactionService;
	
	@GetMapping("/{paymentId}")
	public ResponseEntity<ApiResponse> paymentSuccessHandler(
			@PathVariable String paymentId,
			@RequestParam String paymentLinkId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		
		PaymentLinkResponse paymentResponse;
		
		PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentId);
		
		boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);
		
		if(paymentSuccess) {
			for(Order order:paymentOrder.getOrders()) {
				
				transactionService.createTransaction(order);
				
				Seller seller = sellerService.getSellerById(order.getSellerId());
				SellerReport report = sellerReportService.getSellerReport(seller);
				
				report.setTotalOrders(report.getTotalOrders() + 1);
				report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
				report.setTotalSales(report.getTotalSales() + order.getOrderItems().size());
				sellerReportService.updateSellerReport(report);
			}
		}
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Payment Successful");
		
		return new ResponseEntity<>(res, HttpStatus.OK);	
	}

}
