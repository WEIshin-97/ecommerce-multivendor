package com.snorlax.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.snorlax.modal.Cart;
import com.snorlax.modal.Coupon;
import com.snorlax.modal.User;
import com.snorlax.repository.CartRepository;
import com.snorlax.repository.CouponRespository;
import com.snorlax.repository.ReviewRepository;
import com.snorlax.repository.UserRepository;
import com.snorlax.service.CouponService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{
	
	private final CouponRespository couponRepository;
	private final CartRepository cartRepository;
	private final UserRepository userRepository;

	@Override
	public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
		
		Coupon coupon = couponRepository.findByCode(code);
		
		Cart cart = cartRepository.findByUserId(user.getId());
		
		if(coupon == null) {
			throw new Exception("coupon not valid");
		}
		
		if(user.getUsedCoupons().contains(coupon)) {
			throw new Exception("coupon already used");
		}
		
		if(orderValue < coupon.getMinimumOrderValue()) {
			throw new Exception("Valid for minimun order value: " + coupon.getMinimumOrderValue());
		}
		
		if(coupon.isActive()
			&& LocalDate.now().isAfter(coupon.getValidityStartDate())
			&& LocalDate.now().isBefore(coupon.getValidityEndDate())
		){
			user.getUsedCoupons().add(coupon);
			userRepository.save(user);
			
			double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;
			
			cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
			cart.setCouponCode(code);
			cartRepository.save(cart);
			return cart;
			
		}
			
		throw new Exception("coupon not valid");
	}

	@Override
	public Cart removeCoupon(String code, User user) throws Exception {
		
		Coupon coupon = couponRepository.findByCode(code);
		if(coupon == null) {
			throw new Exception("coupon not found ");
		}
		
		// remove from used coupon
		user.getUsedCoupons().remove(coupon);
		userRepository.save(user);
		
		// remove coupon from cart add back discounted price
		Cart cart = cartRepository.findByUserId(user.getId());
		
		double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;
		
		cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);
		cart.setCouponCode(null);
		
		return cartRepository.save(cart);
	}

	@Override
	public Coupon findCouponById(Long id) throws Exception {
		
		return couponRepository.findById(id).orElseThrow(() -> new Exception("Coupon not found"));
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public Coupon createCoupon(Coupon coupon) {
		
		return couponRepository.save(coupon);
	}

	@Override
	public List<Coupon> findAllCoupons() {
		
		return couponRepository.findAll();
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteCoupon(Long id) throws Exception {
		
		Coupon coupon = findCouponById(id);
		couponRepository.delete(coupon);
	}

}
