package com.snorlax.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.modal.Cart;
import com.snorlax.modal.CartItem;
import com.snorlax.modal.Product;
import com.snorlax.modal.User;
import com.snorlax.request.AddItemRequest;
import com.snorlax.response.ApiResponse;
import com.snorlax.service.CartItemService;
import com.snorlax.service.CartService;
import com.snorlax.service.ProductService;
import com.snorlax.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
	
	private final CartService cartService;
	private final CartItemService cartItemService;
	private final UserService userService;
	private final ProductService productService;
	
	@GetMapping
	public ResponseEntity<Cart> findUserCartHandler(
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		
		Cart cart = cartService.findUserCart(user);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@PutMapping("/add")
	public ResponseEntity<CartItem> addItemToCart(
			@RequestBody AddItemRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		
		Product product = productService.findProductById(req.getProductId());
		
		CartItem item = cartService.addCartItem(user, 
				product, 
				req.getSize(), 
				req.getQuantity()
		);
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Item added to cart successfully");
		
		return new ResponseEntity<CartItem>(item, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/item/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItemHandler(
			@PathVariable Long cartItemId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		cartItemService.deleteCartItem(user.getId(), cartItemId);
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Item remove from the cart");
		
		return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/item/{cartItemId}")
	public ResponseEntity<CartItem> updateCartItemHandler(
			@PathVariable Long cartItemId,
			@RequestBody CartItem cartItem,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
	
		CartItem updatedCartItem = null;
		if(cartItem.getQuantity() > 0) {
			updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
		}
		
		return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
	}

}
