package com.snorlax.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.snorlax.domain.PaymentOrderStatus;
import com.snorlax.domain.PaymentStatus;
import com.snorlax.modal.Order;
import com.snorlax.modal.PaymentOrder;
import com.snorlax.modal.User;
import com.snorlax.repository.OrderRepository;
import com.snorlax.repository.PaymentOrderRepository;
import com.snorlax.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
	
	private final PaymentOrderRepository paymentOrderRepository;
	private final OrderRepository orderRepository;
	
	private String apiKey = "apikey";
	private String apiSecret = "apisecret";
	private String stripeSecretKey = "stripesecretkey";
	
	@Override
	public PaymentOrder createOrder(User user, Set<Order> orders) {
		
		Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();
		
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setAmount(amount);
		paymentOrder.setUser(user);
		paymentOrder.setOrders(orders);
		
		return paymentOrderRepository.save(paymentOrder);
	}

	@Override
	public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {

		return paymentOrderRepository.findById(orderId).orElseThrow(() -> new Exception("payment order not found"));
	}

	@Override
	public PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws Exception {
		
		PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(paymentId);
		
		if(paymentOrder == null) {
			throw new Exception("payment order not found with provided payment link id");
		}
		
		return paymentOrder;
	}

	@Override
	public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) {
		
		if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
			// refer YT 11:11:12
			
//			RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
//			Payment payment = razorpay.payments.fetch(paymentId);
//			
//			String status = payment.get("status");
//			if(status.equals("captured")) {
//				Set<Order> orders = paymentOrder.getOrders();
//				for(Order order:orders) {
//					order.setPaymentStatus(PaymentStatus.COMPLETED);
//					orderRepository.save(order);
//				}
//				
//				paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
//				paymentOrderRepository.save(paymentOrder);
//				return true;
//			}
//			
//			paymentOrder.setStatus(PaymentOrderStatus.FAILED);
//			paymentOrderRepository.save(paymentOrder);
//			return false;
		}
		
		return false;
	}

	@Override
	public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
		
		Stripe.apiKey = stripeSecretKey;
		
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:3000/payment-success/" + orderId)
				.setCancelUrl("http://localhost:3000/payment-cancel")
				.addLineItem(SessionCreateParams.LineItem.builder()
						.setQuantity(1L)
						.setPriceData(SessionCreateParams.LineItem.PriceData.builder()
								.setCurrency("usd")
								.setUnitAmount(amount * 100)
								.setProductData(
										SessionCreateParams
											.LineItem.PriceData.ProductData
											.builder().setName("snorlax payment")
											.build()
								).build()
						).build()
				).build();
		
		Session session = Session.create(params);
		
		return session.getUrl();
	}

}
