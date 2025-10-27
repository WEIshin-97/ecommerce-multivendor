package com.snorlax.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//This defines a custom filter that will be executed once per HTTP request.
//Used in Spring Security’s filter chain (configured in your AppConfig).
public class JwtTokenValidator extends OncePerRequestFilter {

	//The core method where we define what the filter does.
	//FilterChain lets us continue processing the next filters after validation.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = request.getHeader("Authorization");
		
		if(jwt != null) {
			jwt = jwt.substring(7); //Bearer JWT
			
			try {
				//Creates a SecretKey from your secret string constant.
				//JWT_CONSTANT.SECRET_KEY is the same key used to sign the JWT when you created it.
				//Without this key, the token can’t be verified.
				SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes()); 
				
				//Verifies the token’s signature | Checks expiration date | Decodes and extracts the token’s payload (claims)
				Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
						.parseClaimsJws(jwt).getBody();	
				
				//Extracts custom fields you stored in the JWT when generating it
				String email = String.valueOf(claims.get("email"));
				String authorities = String.valueOf(claims.get("authorities"));
				
				//Converts "ROLE_USER,ROLE_ADMIN" into a list of Spring Security GrantedAuthority objects.
				//This is what Spring uses to control access (e.g., @PreAuthorize("hasRole('ADMIN')")).
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				
				//Creates an Authentication object that represents the current user.
				// email = username (principal) | null = credentials (you don’t store passwords here) | auths = list of roles/authorities.
				Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
				
				//Stores your authentication object in the Spring Security context.
				//From now on, the request is treated as authenticated, and controllers can access the user info via:
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			}catch(Exception e) {
				 
				//If any error occurs (invalid token, signature mismatch, expired token), Spring throws this exception.
				//Spring Security will then reject the request with a 401 Unauthorized.
				throw new BadCredentialsException("Invalid JWT Token");
			}
			
		}
		
		//Always called at the end so the request continues to the next filter or controller.
		//Without this, the chain stops here
		filterChain.doFilter(request, response);
		
	}
}

// JOBS
// -------------------------
//Extract JWT from the request header
//Validate and decode it
//Build a Spring Security Authentication object
//Store it in the SecurityContext so downstream code knows who the user is
