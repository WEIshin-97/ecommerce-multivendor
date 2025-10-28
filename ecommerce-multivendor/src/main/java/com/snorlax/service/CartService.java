package com.snorlax.service;

import com.snorlax.modal.Cart;
import com.snorlax.modal.CartItem;
import com.snorlax.modal.Product;
import com.snorlax.modal.User;

public interface CartService {
	
	CartItem addCartItem(
			User user,
			Product product,
			String size,
			Integer quantity
	);
	
	Cart findUserCart(User user);

}
