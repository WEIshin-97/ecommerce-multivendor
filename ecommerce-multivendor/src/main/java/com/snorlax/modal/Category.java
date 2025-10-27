package com.snorlax.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data 
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@NotNull
	@Column(unique = true)
	private String categoryId;
	
	@ManyToOne
	private Category parentCategory;
	
	@NotNull
	// will consist 3 level : 1st -> Men, women, kids, 2nd -> (in Men) Top, Bottom, Shoe, 3rd -> (in Top) suit, t-shirt
	private Integer level;  

}
