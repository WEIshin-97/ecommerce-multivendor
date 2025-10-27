package com.snorlax.modal;

import com.snorlax.domain.PaymentStatus;

import lombok.Data;

@Data
public class PaymentDetails {

	private String paymentId;
	
	private String razorPaymentLinkId;
	
	private String razorPaymentLinkReferenceId;
	
	private String razorPaymentLinlStatus;
	
	private String razorPaymentIdZWSP;
	
	private PaymentStatus status;
	
	
}
