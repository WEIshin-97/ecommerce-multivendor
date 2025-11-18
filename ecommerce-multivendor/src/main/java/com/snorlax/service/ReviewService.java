package com.snorlax.service;

import java.util.List;

import com.snorlax.modal.Product;
import com.snorlax.modal.Review;
import com.snorlax.modal.User;
import com.snorlax.request.CreateReviewRequest;

public interface ReviewService {
	
	Review createReview(CreateReviewRequest req,
			User user,
			Product product);
	
	List<Review> getReviewByProductId(Long productId);
	
	Review updateReview(Long reviewId, String reviewtext, double rating, Long userId) throws Exception;
	
	void deleteReview(Long reviewId,Long userId) throws Exception;
	
	Review getReviewById(Long reviewId) throws Exception;
	
	

}
