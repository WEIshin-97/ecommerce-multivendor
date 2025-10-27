package com.snorlax.request;

import com.snorlax.modal.Address;
import com.snorlax.modal.BankDetails;
import com.snorlax.modal.BusinessDetails;

import lombok.Data;

@Data
public class SignUpRequest {
	
	private String email;
	
	private String fullName;
	
	private String otp;
	
	private String role;
	
	
	// for seller
	private String mobile;
    private String GSTIN;
    private BusinessDetails businessDetails;
    private BankDetails bankDetails;
    private Address pickupAddress;
	
	

}
