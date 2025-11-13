package com.snorlax.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.modal.Coupon;

public interface CouponRespository extends JpaRepository<Coupon, Long> {
	
	Coupon findByCode(String code);

}
