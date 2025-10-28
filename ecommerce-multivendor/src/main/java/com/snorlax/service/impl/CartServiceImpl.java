package com.snorlax.service.impl;

import org.springframework.stereotype.Service;

import com.snorlax.repository.CartRepository;
import com.snorlax.modal.Cart;
import com.snorlax.modal.CartItem;
import com.snorlax.modal.Product;
import com.snorlax.modal.User;
import com.snorlax.repository.CartItemRepository;
import com.snorlax.service.CartService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
	
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	
	@Override
	public CartItem addCartItem(User user, Product product, String size, Integer quantity) {
		
		Cart cart = findUserCart(user);
		
		CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
		
		if(isPresent == null) {
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cartItem.setUserId(user.getId());
			cartItem.setSize(size);
			
			int totalPrice = quantity * product.getSelingPrice();
			int totalMrpPrice = quantity * product.getMrpPrice();
			cartItem.setSellingPrice(totalPrice);
			cartItem.setMrpPrice(totalMrpPrice);
			
			cart.getCartItems().add(cartItem);
			cartItem.setCart(cart);
			
			return cartItemRepository.save(cartItem);
		}
		
		
		return isPresent;
	}
	@Override
	public Cart findUserCart(User user) {
		
		Cart cart = cartRepository.findByUserId(user.getId());
		 
		int totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItem = 0;
		
		for(CartItem cartItem: cart.getCartItems()) {
			totalPrice += cartItem.getMrpPrice() != null ? cartItem.getMrpPrice() : 0;
			totalDiscountedPrice += cartItem.getSellingPrice() != null ? cartItem.getSellingPrice() : 0;
			totalItem += cartItem.getQuantity();
		}
		
		cart.setTotalMrpPrice(totalPrice);
		cart.setTotalSellingPrice(totalDiscountedPrice);
		cart.setTotalItem(totalItem);
		cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));
		
		return cart;
	}
	
	private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
		
		if(mrpPrice <= 0) {
			return 0;
		}
		double discount = mrpPrice - sellingPrice;
		double discountPercentage = (discount/mrpPrice) * 100;
		
		return (int)discountPercentage;
	}

}
