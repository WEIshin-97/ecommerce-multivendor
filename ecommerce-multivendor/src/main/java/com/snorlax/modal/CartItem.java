package com.snorlax.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data 
public class CartItem {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JsonIgnore //to ignore a field when serializing to JSON.
	private Cart cart;
	
	@ManyToOne
	private Product product;
	
	private String size;  // S, M, L
	
	private int quantity=1;
	
	private Integer mrpPrice;
	
	private Integer sellingPrice;
	
	private Long userId;

}
