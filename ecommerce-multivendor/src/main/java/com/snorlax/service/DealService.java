package com.snorlax.service;

import java.util.List;

import com.snorlax.modal.Deal;

public interface DealService {
	
	List<Deal> getDeals();
	
	Deal createDeal(Deal deal);
	
	Deal updateDeal(Long id, Deal deal) throws Exception;
	
	void deleteDeal(Long id) throws Exception;

}
