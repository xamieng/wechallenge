package com.wechallenge.review.dto

import javax.validation.constraints.NotNull

class ReviewDTO {
    @NotNull
    var reviewId: Int? = null
    var review: String? = null

    override fun toString() = "ReviewDTO = { reviewId: $reviewId, review: $review }"
}