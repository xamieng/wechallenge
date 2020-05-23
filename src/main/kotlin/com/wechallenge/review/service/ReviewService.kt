package com.wechallenge.review.service

import com.wechallenge.review.domain.Review
import com.wechallenge.review.dto.UpdateReviewDTO
import com.wechallenge.review.repository.ReviewRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.SearchHit
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewService (
        private val reviewRepository: ReviewRepository
) {

    private val logger = LoggerFactory.getLogger(ReviewService::class.java)

    @Transactional(readOnly = true)
    fun getReviewById(reviewId: Int): Review? {
        logger.debug("getReviewById: $reviewId")
        return reviewRepository.findOneByReviewId(reviewId)
    }

    @Transactional(readOnly = true)
    fun searchReviewByFoodText(query: String, pageable: Pageable): List<SearchHit<Review>> {
        logger.debug("searchReviewByFoodText: $query")
        return reviewRepository.searchReviewByFoodKeyword(query, pageable)
    }

    @Transactional
    fun updateReview(review: Review, dto: UpdateReviewDTO): Review {
        logger.debug("updateReview: $review with $dto")
        review.review = dto.review
        return reviewRepository.save(review)
    }
}