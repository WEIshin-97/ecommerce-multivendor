package com.snorlax.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.snorlax.exception.ProductException;
import com.snorlax.modal.Product;
import com.snorlax.modal.Seller;
import com.snorlax.request.CreateProductRequest;

public interface ProductService {
	
	Product createProduct(CreateProductRequest req, Seller seller);
	
	void deleteProduct(Long productId) throws ProductException;
	
	Product updateProduct(Long productId, Product product) throws ProductException;
	
	Product findProductById(Long productId) throws ProductException;
	
	List<Product> searchProducts(String query);
	
	Page<Product> getAllProducts(
			String category,
			String brand,
			String colors,
			String sizes,
			Integer minPrice,
			Integer maxPrice,
			Integer minDiscount,
			String sort,
			String stock,
			Integer pageNumber
	);
	
	List<Product> getProductsBySellerId(Long sellerId);

}
