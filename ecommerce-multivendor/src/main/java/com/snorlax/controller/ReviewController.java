package com.snorlax.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snorlax.modal.Product;
import com.snorlax.modal.Review;
import com.snorlax.modal.User;
import com.snorlax.request.CreateReviewRequest;
import com.snorlax.response.ApiResponse;
import com.snorlax.service.ProductService;
import com.snorlax.service.ReviewService;
import com.snorlax.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
	
	private final ReviewService reviewService;
	private final UserService userService;
	private final ProductService productService;
	
	@GetMapping("/products/{productId}/reviews")
	public ResponseEntity<List<Review>> getAllOrdersHandler(
			@PathVariable Long productId){
		
		List<Review> reviews = reviewService.getReviewByProductId(productId);
		
		return new ResponseEntity<>(reviews, HttpStatus.OK);
	}
	
	@PostMapping("/products/{productId}/reviews")
	public ResponseEntity<Review> writeReview(
			@PathVariable Long productId,
			@RequestParam CreateReviewRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		Product product = productService.findProductById(productId);
		
		Review review = reviewService.createReview(req, user, product);
		
		return new ResponseEntity<>(review, HttpStatus.ACCEPTED);
	}
	
	@PatchMapping("/reviews/{reviewId}")
	public ResponseEntity<Review> updateReview(
			@PathVariable Long reviewId,
			@RequestParam CreateReviewRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		
		Review review = reviewService.updateReview(reviewId, req.getReviewText(), req.getRating(), user.getId());
		
		return new ResponseEntity<>(review, HttpStatus.OK);
	}
	
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<ApiResponse> deleteReview(
			@PathVariable Long reviewId,
			@RequestParam CreateReviewRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		
		reviewService.deleteReview(reviewId, user.getId());
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Delete Review Successfully");
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

}
