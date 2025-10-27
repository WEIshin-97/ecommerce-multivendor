package com.snorlax.service;

import com.snorlax.response.AuthResponse;
import com.snorlax.request.LoginRequest;
import com.snorlax.request.SignUpRequest;

public interface AuthService {
	
	String createUser(SignUpRequest req);
	
	AuthResponse signIn(LoginRequest req);

}
