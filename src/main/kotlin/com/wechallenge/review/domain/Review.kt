package com.wechallenge.review.domain

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "wechallenge", type = "review")
class Review {
    @Id
    var id: String? = null
    var reviewId: Int? = null
    var review: String? = null
    override fun toString() = "Review = { reviewId: $reviewId, review: $review }"
}