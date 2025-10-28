package com.snorlax.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.modal.Cart;
import com.snorlax.modal.CartItem;
import com.snorlax.modal.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	
	//it means 
	//SELECT * 
	//FROM cart_item 
	//WHERE cart_id = ? AND product_id = ? AND size = ?;
	CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);

}
