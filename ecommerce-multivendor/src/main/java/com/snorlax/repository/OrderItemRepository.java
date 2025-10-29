package com.snorlax.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.modal.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
