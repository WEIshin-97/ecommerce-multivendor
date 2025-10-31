package com.snorlax.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.modal.PaymentOrder;

public interface PaymentOrderRepository  extends JpaRepository<PaymentOrder, Long>{

	PaymentOrder findByPaymentLinkId(String paymentLinkId);
}
