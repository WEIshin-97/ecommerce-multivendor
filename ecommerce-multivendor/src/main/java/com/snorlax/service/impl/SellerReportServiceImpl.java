package com.snorlax.service.impl;

import org.springframework.stereotype.Service;

import com.snorlax.modal.Seller;
import com.snorlax.modal.SellerReport;
import com.snorlax.repository.SellerReportRepository;
import com.snorlax.service.SellerReportService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService{
	
	private final SellerReportRepository sellerReportRepository;

	@Override
	public SellerReport getSellerReport(Seller seller) {
		
		SellerReport sr = sellerReportRepository.findBySellerId(seller.getId());
		
		if(sr == null) {
			SellerReport newReport = new SellerReport();
			newReport.setSeller(seller);
			return sellerReportRepository.save(newReport);
		}
		return sr;
	}

	@Override
	public SellerReport updateSellerReport(SellerReport sellerReport) {
		
		return sellerReportRepository.save(sellerReport);
	}

}
