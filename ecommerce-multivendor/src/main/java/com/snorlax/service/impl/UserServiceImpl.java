package com.snorlax.service.impl;

import org.springframework.stereotype.Service;

import com.snorlax.config.JwtProvider;
import com.snorlax.modal.User;
import com.snorlax.repository.UserRepository;
import com.snorlax.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;
	
	@Override
	public User findByJwtToken(String jwt) throws Exception {
		
		String email = jwtProvider.getEmailFromJwtToken(jwt);
		
		return this.findUserByEmail(email);
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			throw new Exception("user not found by email "+ email);
		}
		
		return user;
	}

}
