package com.snorlax.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.modal.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
	
	Cart findByUserId(Long id);

}
