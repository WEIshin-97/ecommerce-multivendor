package com.snorlax.config;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
	
	SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes()); 
	
	public String generateToken(Authentication auth) {
		
		//Fetches all the user’s roles or permissions (e.g. ROLE_ADMIN, ROLE_USER)
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		//Calls your helper method populateAuthorities() to turn a collection of authorities into a comma-separated string (like "ROLE_ADMIN,ROLE_USER").
		String roles = populateAuthorities(authorities);
		
		//.setIssuedAt(new Date()) — sets the time when the token was generated.
		//.setExpiration(new Date(new Date().getTime()+86400000)) — sets token validity to 24 hours (in milliseconds).
		//.claim("name", auth.getName()) — adds a custom claim “name” which is usually the username or email.
		//.claim("authorities", roles) — adds another custom claim containing user’s roles.
		//.signWith(key) — signs the token with your secret key to make it tamper-proof.
		//.compact() — generates and returns the final JWT string.
		// Result: You get a string like eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiYWRtaW4iLCJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4ifQ..
		return Jwts.builder()
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+86400000))
				.claim("name", auth.getName())
				.claim("authorities", roles)
				.signWith(key)
				.compact();
	}
	
	public String getEmailFromJwtToken(String jwt) {
		jwt = jwt.substring(7);
		
		//Parses and validates the token using your secret key.
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
				.parseClaimsJws(jwt).getBody();	
		
		//Extracts custom fields you stored in the JWT when generating it
		return String.valueOf(claims.get("name"));
	}

	//Helper: Convert Authorities to String
	//Iterates over all user authorities (roles).
	//Collects them into a Set (to avoid duplicates).
	//Joins them with commas — e.g., "ROLE_USER,ROLE_ADMIN".
	//This string is stored inside the JWT as a claim.
	private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<String> auths = new HashSet<>();
		
		for(GrantedAuthority authority:authorities) {
			auths.add(authority.getAuthority());
		}
		
		return String.join(",", auths);
	}

}
