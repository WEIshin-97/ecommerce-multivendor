package com.snorlax.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snorlax.config.JwtProvider;
import com.snorlax.modal.Product;
import com.snorlax.modal.Review;
import com.snorlax.modal.User;
import com.snorlax.repository.ReviewRepository;
import com.snorlax.request.CreateReviewRequest;
import com.snorlax.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
	
	private final ReviewRepository reviewRepository;

	@Override
	public Review createReview(CreateReviewRequest req, User user, Product product) {
		
		Review review = new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReviewText(req.getReviewText());
		review.setRating(req.getRating());
		review.setProductImages(req.getProductImages());
		
		product.getReviews().add(review);
		
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getReviewByProductId(Long productId) {
		
		return reviewRepository.findByProductId(productId);
	}

	@Override
	public Review updateReview(Long reviewId, String reviewtext, double rating, Long userId) throws Exception {
		
		Review review = getReviewById(reviewId);
		
		if(review.getUser().getId().equals(userId)) {
			review.setReviewText(reviewtext);
			review.setRating(rating);
			
			return reviewRepository.save(review);
		}
		
		throw new Exception("Not allow to update review");
	}

	@Override
	public void deleteReview(Long reviewId, Long userId) throws Exception {
		Review review = getReviewById(reviewId);
		
		if(!review.getUser().getId().equals(userId)) {
			throw new Exception("Not allow to delete review");
		}
		
		reviewRepository.delete(review);
	}

	@Override
	public Review getReviewById(Long reviewId) throws Exception {
				
		return reviewRepository.findById(reviewId).orElseThrow(() -> new Exception("Review not found"));
	}

}
