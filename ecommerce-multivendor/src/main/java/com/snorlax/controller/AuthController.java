package com.snorlax.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.domain.USER_ROLE;
import com.snorlax.response.AuthResponse;
import com.snorlax.request.LoginRequest;
import com.snorlax.request.SignUpRequest;
import com.snorlax.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> signupHandler(@RequestBody SignUpRequest req){
		
		String jwt = authService.createUser(req);
		
		AuthResponse res = new AuthResponse();
		res.setJwt(jwt);
		res.setMessage("register "+ req.getRole() +" success");
		res.setRole(USER_ROLE.valueOf("ROLE_" + req.getRole()));
		
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req){
		
		AuthResponse res = authService.signIn(req);
		
		return ResponseEntity.ok(res);
	}

}
