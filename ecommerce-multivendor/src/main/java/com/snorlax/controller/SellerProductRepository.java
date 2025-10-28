package com.snorlax.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.exception.ProductException;
import com.snorlax.modal.Product;
import com.snorlax.modal.Seller;
import com.snorlax.request.CreateProductRequest;
import com.snorlax.service.ProductService;
import com.snorlax.service.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/products")
public class SellerProductRepository {
	
	private final ProductService productService;
	private final SellerService sellerService;
	
	@GetMapping
	public ResponseEntity<List<Product>> getProductBySellerId(
			@RequestHeader("Authorization") String jwt) throws Exception {
		
		Seller seller = sellerService.getSellerProfile(jwt);
		
		List<Product> products = productService.getProductsBySellerId(seller.getId());
		
		return new ResponseEntity<>(products, HttpStatus.OK);	
	}
	
	@PostMapping
	public ResponseEntity<Product> createProduct(
			@RequestBody CreateProductRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception {
		
		Seller seller = sellerService.getSellerProfile(jwt);
		
		Product product = productService.createProduct(req, seller);
		
		return new ResponseEntity<>(product, HttpStatus.OK);	
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<Product> deleteProduct(
			@PathVariable Long productId) {
		
		try {
			productService.deleteProduct(productId);
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(ProductException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}	
	}
	
	@PutMapping("/{productId}")
	public ResponseEntity<Product> updateProduct(
			@PathVariable Long productId,
//			@RequestBody CreateProductRequest product) {
			@RequestBody Product product) {
		
		try {
			Product updatedProduct = productService.updateProduct(productId, product);
			return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
			
		}catch(ProductException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}	
	}

}
