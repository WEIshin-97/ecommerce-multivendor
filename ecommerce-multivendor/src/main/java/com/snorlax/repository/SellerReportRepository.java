package com.snorlax.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.modal.SellerReport;

public interface SellerReportRepository extends JpaRepository<SellerReport, Long>{
	
	SellerReport findBySellerId(Long sellerId);

}
