package com.wechallenge.review.assembler

import com.wechallenge.review.domain.Review
import com.wechallenge.review.dto.ReviewDTO
import org.springframework.data.elasticsearch.core.SearchHit
import org.springframework.stereotype.Component

@Component
class ReviewAssembler {
    fun assembleDTO(review: Review): ReviewDTO {
        val dto = ReviewDTO()
        dto.reviewId = review.reviewId
        dto.review = review.review
        return dto
    }

    fun assembleDTO(review: SearchHit<Review>): ReviewDTO {
        val dto = ReviewDTO()
        dto.reviewId = review.content.reviewId
        dto.review = review.highlightFields["review"]?.let { highlights ->
            if (highlights.isEmpty()) review.content.review else highlights.first()
        }
        return dto
    }

    fun assembleDomain(dto: ReviewDTO): Review {
        val review = Review()
        review.reviewId = dto.reviewId
        review.review = dto.review
        return review
    }
}