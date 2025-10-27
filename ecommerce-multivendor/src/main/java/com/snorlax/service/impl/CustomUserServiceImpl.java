package com.snorlax.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.snorlax.domain.USER_ROLE;
import com.snorlax.modal.Seller;
import com.snorlax.modal.User;
import com.snorlax.repository.SellerRepository;
import com.snorlax.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//after this service file, jwt password wont show in terminal again
@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService{

	private final UserRepository userRepository;
	private final SellerRepository sellerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String combinedUsername) throws UsernameNotFoundException {
	
		String[] parts = combinedUsername.split(":", 2);
	    String rolePart = (parts.length > 1) ? parts[0] : USER_ROLE.ROLE_CUSTOMER.toString();
	    String username = (parts.length > 1) ? parts[1] : combinedUsername;
	    
		if(USER_ROLE.ROLE_SELLER == USER_ROLE.valueOf(rolePart)) {
			
			Seller seller = sellerRepository.findByEmail(username);
			
			if(seller != null) {
				return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
			}
			
		}else {
			User user = userRepository.findByEmail(username);
			
			if(user != null) {
				return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
			}
		}
		
		throw new UsernameNotFoundException("user or seller not found with email" + username);
	}

	private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
		

		if(role == null) role = USER_ROLE.ROLE_CUSTOMER;
		
		List<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority(role.toString()));
		
		return new org.springframework.security.core.userdetails.User(email, password, authorityList);
	}

}
