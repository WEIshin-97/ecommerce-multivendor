package com.snorlax.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.modal.Cart;
import com.snorlax.modal.Coupon;
import com.snorlax.modal.Review;
import com.snorlax.modal.User;
import com.snorlax.service.CartService;
import com.snorlax.service.CouponService;
import com.snorlax.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class CouponController {
	
	private final UserService userService;
	private final CouponService couponService;
	private final CartService cartService;
	
	@PostMapping("/apply")
	public ResponseEntity<Cart> applyCoupon(
			@RequestParam String apply,
			@RequestParam String code,
			@RequestParam double orderValue,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		
		Cart cart;
		if(apply.equals("true")) {
			
			cart = couponService.applyCoupon(code, orderValue, user);
		}else {
			
			cart = couponService.removeCoupon(code, user);
		}
		
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/admin/create")
	public ResponseEntity<Coupon> createCoupon(
			@RequestBody Coupon coupon){
		
		Coupon cretedCoupon = couponService.createCoupon(coupon);
		return ResponseEntity.ok(cretedCoupon);
	}
	
	@DeleteMapping("/admin/delete/{id}")
	public ResponseEntity<?> deleteCoupon(
			@PathVariable Long id) throws Exception{
		
		couponService.deleteCoupon(id);
		return ResponseEntity.ok("Coupon deleted successfully");
	}
	
	@GetMapping("/admin/all")
	public ResponseEntity<List<Coupon>> getAllCoupons(){
			
		List<Coupon> coupons = couponService.findAllCoupons();
		return ResponseEntity.ok(coupons);
	}

}
