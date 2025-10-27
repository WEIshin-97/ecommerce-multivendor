package com.snorlax.modal;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.snorlax.domain.USER_ROLE;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode 
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	//You can send it in requests (like during registration/login).
	//It will NOT appear in responses (prevents leaking passwords to the frontend)
	private String password;
	
	private String email;
	
	private String fullName;
	
	private String mobile;
	
	private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;
	
	@OneToMany
	//Stores a set of addresses related to the user
	private Set<Address> addresses = new HashSet<>();
	
	@ManyToMany
	@JsonIgnore
	//that field will not appear in the JSON output when your object is sent as a response to the frontend.
	//It also means that when JSON data comes from the frontend,
	//that field will be ignored during deserialization (it won’t be read from the JSON).
	private Set<Coupon> usedCoupons = new HashSet<>();

}
