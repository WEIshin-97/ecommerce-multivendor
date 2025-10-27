package com.snorlax.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.snorlax.config.JwtProvider;
import com.snorlax.domain.USER_ROLE;
import com.snorlax.modal.Address;
import com.snorlax.modal.Cart;
import com.snorlax.modal.Seller;
import com.snorlax.modal.User;
import com.snorlax.repository.AddressRepository;
import com.snorlax.repository.CartRepository;
import com.snorlax.repository.SellerRepository;
import com.snorlax.repository.UserRepository;
import com.snorlax.request.LoginRequest;
import com.snorlax.request.SignUpRequest;
import com.snorlax.response.AuthResponse;
import com.snorlax.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	private final UserRepository userRepository;
	private final SellerRepository sellerRepository;
	private final AddressRepository addressRepository;
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;
	private final JwtProvider jwtProvider;
	private final CustomUserServiceImpl customUserServiceImpl;

	@Override
	public String createUser(SignUpRequest req) {
		
		String roleInput = req.getRole(); // "CUSTOMER" or "SELLER"
		USER_ROLE role = USER_ROLE.valueOf("ROLE_" + roleInput.toUpperCase());
		
	    if (USER_ROLE.ROLE_SELLER.equals(role)) {
	        createSellerAccount(req);
	    } else {
	        createCustomerAccount(req);
	    }
	    
	    List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.toString()));
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return jwtProvider.generateToken(authentication);
	}

	private void createCustomerAccount(SignUpRequest req) {
		User user = userRepository.findByEmail(req.getEmail());
		
		if(user == null) {
			
			User createdUser = new User();
			createdUser.setEmail(req.getEmail());
			createdUser.setFullName(req.getFullName());
			createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
			createdUser.setMobile("0123456789");
			createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
			
			user = userRepository.save(createdUser);
			
			Cart cart = new Cart();
			cart.setUser(createdUser);
			cartRepository.save(cart);
			
		}
	}

	private void createSellerAccount(SignUpRequest req) {
		
		Seller seller = sellerRepository.findByEmail(req.getEmail());
		
		if(seller == null) {
			Seller newSeller = new Seller();
			newSeller.setEmail(req.getEmail());
			newSeller.setPassword(passwordEncoder.encode(req.getOtp()));
			newSeller.setFullName(req.getFullName());
			newSeller.setGSTIN(req.getGSTIN());
			newSeller.setRole(USER_ROLE.ROLE_SELLER);
			newSeller.setMobile(req.getMobile());
			newSeller.setBankDetails(req.getBankDetails());
			newSeller.setBusinessDetails(req.getBusinessDetails());
			
			Address savedAddr = addressRepository.save(req.getPickupAddress());
			newSeller.setPickupAddress(savedAddr);
			
			sellerRepository.save(newSeller);
			
		}
	}

	@Override
	public AuthResponse signIn(LoginRequest req) {
		String username = req.getEmail();
		String otp = req.getOtp();
		
		String roleInput = req.getRole(); // "CUSTOMER" or "SELLER"
		USER_ROLE role = USER_ROLE.valueOf("ROLE_" + roleInput.toUpperCase());
		
		Authentication authentication = authenticate(username, otp, role);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Login "+ roleInput +" Success");
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
		
		authResponse.setRole(USER_ROLE.valueOf(roleName));
		
		return authResponse;
	}

	private Authentication authenticate(String username, String otp, USER_ROLE role) {
		
		String combinedUsername = role + ":" + username;
		
		UserDetails userDetails = customUserServiceImpl.loadUserByUsername(combinedUsername);
		
		if(userDetails == null) {
			throw new BadCredentialsException("invalid username");
		}
		
		// Compare encoded password in DB with raw OTP from login request
	    if (!passwordEncoder.matches(otp, userDetails.getPassword())) {
	        throw new BadCredentialsException("Invalid otp or password");
	    }
		
		return new UsernamePasswordAuthenticationToken(
				userDetails, 
				null, 
				userDetails.getAuthorities() 
		);
	}

}
