package com.snorlax.service;

import com.snorlax.modal.User;

public interface UserService {
	
	User findByJwtToken(String jwt) throws Exception;
	User findUserByEmail(String email) throws Exception;

}
