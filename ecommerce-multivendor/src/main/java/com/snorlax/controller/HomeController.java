package com.snorlax.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.response.ApiResponse;

@RestController
public class HomeController {
	
	@GetMapping("/hello")
	public ApiResponse displayHome() {
		
		ApiResponse resp = new ApiResponse();
		resp.setMessage("Hello Ecommerce");
		
		return resp;
	}

}
