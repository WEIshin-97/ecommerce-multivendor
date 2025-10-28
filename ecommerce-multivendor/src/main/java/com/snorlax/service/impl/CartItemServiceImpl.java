package com.snorlax.service.impl;

import org.springframework.stereotype.Service;

import com.snorlax.modal.CartItem;
import com.snorlax.modal.User;
import com.snorlax.repository.CartItemRepository;
import com.snorlax.service.CartItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService{
	
	private final CartItemRepository cartItemRepository;
	
	
	@Override
	public CartItem updateCartItem(Long userId, long id, CartItem cartItem) throws Exception {
		
		CartItem item = findCartItemById(id);
		
		User cartItemuser = item.getCart().getUser();
		
		if(cartItemuser.getId().equals(userId)) {
			item.setQuantity(cartItem.getQuantity());
			item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
			item.setSellingPrice(item.getQuantity() * item.getProduct().getSelingPrice());
			
			return cartItemRepository.save(item);
		}else {
			throw new Exception("you can't update the cart item");
		}
	}
	@Override
	public void deleteCartItem(Long userId, long cartItemId) throws Exception {
		CartItem item = findCartItemById(cartItemId);
		
		User cartItemuser = item.getCart().getUser();
		
		if(cartItemuser.getId().equals(userId)) {
			cartItemRepository.delete(item);
		}else {
			throw new Exception("you can't delete the cart item");
		}
		
		
	}
	@Override
	public CartItem findCartItemById(Long id) throws Exception {
		
		return cartItemRepository.findById(id)
				.orElseThrow(() -> new Exception("Cart Item no exist with id: " + id));
	}

}
