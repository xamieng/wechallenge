package com.wechallenge.review.dto

import javax.validation.constraints.NotNull

class UpdateReviewDTO {
    @NotNull
    var review: String? = null

    override fun toString() = "UpdateReviewDTO = { review: $review }"

}