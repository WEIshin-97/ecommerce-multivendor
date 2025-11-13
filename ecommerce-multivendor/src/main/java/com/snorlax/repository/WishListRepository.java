package com.snorlax.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.modal.WishList;

public interface WishListRepository  extends JpaRepository<WishList, Long>{
	
	WishList findByUserId(Long userId);

}
