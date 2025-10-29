package com.snorlax.service;

import java.util.List;
import java.util.Set;

import com.snorlax.domain.OrderStatus;
import com.snorlax.modal.Address;
import com.snorlax.modal.Cart;
import com.snorlax.modal.Order;
import com.snorlax.modal.OrderItem;
import com.snorlax.modal.User;

public interface OrderService {
	
	Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
	
	Order findOrderById(Long id) throws Exception;
	
	List<Order> userOrderHistory(Long userId);
	
	List<Order> sellerOrder(Long sellerId);
	
	Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
	
	Order cancelOrder(Long orderId, User user) throws Exception;
	
	OrderItem getOrderItemById(Long id) throws Exception;
	

}
