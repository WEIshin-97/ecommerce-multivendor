package com.snorlax.service;

import com.snorlax.modal.Seller;
import com.snorlax.modal.SellerReport;

public interface SellerReportService {

	SellerReport getSellerReport(Seller seller);
	
	SellerReport updateSellerReport(SellerReport sellerReport);
}
