package com.snorlax.service;

import com.snorlax.modal.CartItem;

public interface CartItemService {
	
	CartItem updateCartItem(Long userId, long id, CartItem cartItem) throws Exception;
	
	void deleteCartItem(Long userId, long cartItemId) throws Exception;
	
	CartItem findCartItemById(Long id) throws Exception;
	
}
