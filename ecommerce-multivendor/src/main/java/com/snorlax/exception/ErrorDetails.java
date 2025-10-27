package com.snorlax.exception;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorDetails {
	
	private String error;
	
	private String details;
	
	private LocalDateTime timestamp;

}
