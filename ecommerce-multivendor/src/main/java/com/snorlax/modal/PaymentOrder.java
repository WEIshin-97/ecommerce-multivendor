package com.snorlax.modal;

import java.util.HashSet;
import java.util.Set;

import com.snorlax.domain.PaymentMethod;
import com.snorlax.domain.PaymentOrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Long amount;
	
	private PaymentOrderStatus status = PaymentOrderStatus.PENDING;
	
	private PaymentMethod method;
	
	private String paymentLink;
	
	@ManyToOne
	private User user;
	
	@OneToMany
	private Set<Order> orders = new HashSet<>();
	
	
	

}
