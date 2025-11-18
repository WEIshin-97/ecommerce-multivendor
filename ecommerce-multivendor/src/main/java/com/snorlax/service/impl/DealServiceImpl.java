package com.snorlax.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snorlax.modal.Deal;
import com.snorlax.modal.HomeCategory;
import com.snorlax.repository.DealRepository;
import com.snorlax.repository.HomeCategoryRepository;
import com.snorlax.service.DealService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService{
	
	private final DealRepository dealRepository;
	private final HomeCategoryRepository homeCategoryRepository;

	@Override
	public List<Deal> getDeals() {
		
		return dealRepository.findAll();
	}

	@Override
	public Deal createDeal(Deal deal) {
		
		HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
		
		Deal newDeal = dealRepository.save(deal);
		newDeal.setCategory(category);
		newDeal.setDiscount(deal.getDiscount());
		
		return dealRepository.save(newDeal);
	}

	@Override
	public Deal updateDeal(Long id, Deal deal) throws Exception {
		
		Deal existDeal = dealRepository.findById(id).orElse(null);
		HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
		
		if(existDeal != null) {
			if(deal.getDiscount() != null) {
				existDeal.setDiscount(deal.getDiscount());
			}
			
			if(category != null) {
				existDeal.setCategory(category);
			}
			return dealRepository.save(deal);
		}
		
		throw new Exception("Deal not found");
	}

	
	public void deleteDeal(Long id) throws Exception {
		
		Deal deal = dealRepository.findById(id).orElseThrow(() -> new Exception("Deal not found"));
		dealRepository.delete(deal);
	}

}
