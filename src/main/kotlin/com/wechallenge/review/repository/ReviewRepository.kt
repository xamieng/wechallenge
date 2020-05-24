package com.wechallenge.review.repository

import com.wechallenge.review.domain.Review
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Highlight
import org.springframework.data.elasticsearch.annotations.HighlightField
import org.springframework.data.elasticsearch.annotations.HighlightParameters
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.core.SearchHit
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface ReviewRepository: ElasticsearchRepository<Review, String> {
    @Query("{\"bool\": {\"must\": [{\"match\": {\"review\": \"?0\"}}]}}")
    @Highlight(
            fields = [HighlightField(name = "review")] ,
            parameters = HighlightParameters(preTags = ["<keyword>"], postTags = ["</keyword>"], numberOfFragments = 0))
    fun searchReviewByFoodKeyword(keyword: String, pageable: Pageable): List<SearchHit<Review>>
    fun findOneByReviewId(reviewId: Int): Review?
}