package com.snorlax.service;

import com.snorlax.modal.Product;
import com.snorlax.modal.User;
import com.snorlax.modal.WishList;

public interface WishListService {
	
	WishList createWishList(User user);
	
	WishList getWishListByUserId(User user);
	
	WishList addProductToWishList(User user, Product product);
	

}
