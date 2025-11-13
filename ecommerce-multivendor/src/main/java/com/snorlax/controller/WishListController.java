package com.snorlax.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.modal.Product;
import com.snorlax.modal.Transaction;
import com.snorlax.modal.User;
import com.snorlax.modal.WishList;
import com.snorlax.service.ProductService;
import com.snorlax.service.UserService;
import com.snorlax.service.WishListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {
	
	private final WishListService wishListService;
	private final UserService userService;
	private final ProductService productService;
	
	@GetMapping
	public ResponseEntity<WishList> getWishListByUserId(
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		WishList wishlists = wishListService.getWishListByUserId(user);
		
		return new ResponseEntity<>(wishlists, HttpStatus.OK);	
		
	}
	
	@PostMapping("/add-product/{productId}")
	public ResponseEntity<WishList> addProductToWishList(
			@PathVariable Long productId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		Product product = productService.findProductById(productId);
		WishList updatedWishList = wishListService.addProductToWishList(user, product);
		
		return new ResponseEntity<>(updatedWishList, HttpStatus.OK);	
		
	}

}
