package com.snorlax.service;

import java.util.Set;

import com.snorlax.modal.Order;
import com.snorlax.modal.PaymentOrder;
import com.snorlax.modal.User;
import com.stripe.exception.StripeException;

public interface PaymentService {
	
	PaymentOrder createOrder(User user, Set<Order> orders);
	
	PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
	
	PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws Exception;
	
	Boolean proceedPaymentOrder(PaymentOrder paymentOrder,
								String paymentId,
								String paymentLinkId);
	
//	PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId);
	
	String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
	

}
