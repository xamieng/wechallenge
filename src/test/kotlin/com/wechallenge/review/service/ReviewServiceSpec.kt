package com.wechallenge.review.service

import com.wechallenge.review.domain.Review
import com.wechallenge.review.dto.UpdateReviewDTO
import com.wechallenge.review.repository.ReviewRepository
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.SearchHit

@Suppress("DEPRECATION")
@RunWith(MockitoJUnitRunner::class)
class ReviewServiceSpec {
    @Mock
    private lateinit var reviewRepository: ReviewRepository
    private lateinit var reviewService: ReviewService

    @Before
    fun setup() {
        reviewService = ReviewService(reviewRepository)
    }

    @Test
    fun testGetReviewById_when_success() {
        `when`(reviewRepository.findOneByReviewId(ArgumentMatchers.anyInt())).thenReturn(getMockReview())

        val actual = reviewService.getReviewById(1)

        actual?.let {
            Assertions.assertThat(it.review).isEqualTo("review")
            Assertions.assertThat(it.reviewId).isEqualTo(1)
        }
    }

    @Test
    fun testGetReviewById_when_not_found() {
        `when`(reviewRepository.findOneByReviewId(ArgumentMatchers.anyInt())).thenReturn(null)
        val actual = reviewService.getReviewById(1)
        Assertions.assertThat(actual).isEqualTo(null)
    }

    @Test
    fun testSearchReviewByFoodText_when_success() {
        `when`(reviewRepository.searchReviewByFoodKeyword("test", Pageable.unpaged()))
                .thenReturn(getMockSearchHits())

        val actual = reviewService.searchReviewByFoodText("test", Pageable.unpaged())

        actual.let {
            Assertions.assertThat(it.first().highlightFields["review"]).isEqualTo(listOf("<keyword>review</keyword>"))
            Assertions.assertThat(it.first().content.review).isEqualTo("review")
            Assertions.assertThat(it.first().content.reviewId).isEqualTo(1)
        }
    }

    @Test
    fun testSearchReviewByFoodText_when_not_found() {
        `when`(reviewRepository.searchReviewByFoodKeyword("test", Pageable.unpaged()))
                .thenReturn(listOf())

        val actual = reviewService.searchReviewByFoodText("test", Pageable.unpaged())

        Assertions.assertThat(actual.size).isEqualTo(0)
    }

    @Test
    fun testUpdateReview_when_success() {
        `when`(reviewRepository.save(anyObject<Review>())).thenReturn(getMockReview())

        val actual = reviewService.updateReview(getMockReview(), getMockUpdatedReviewDTO())

        Assertions.assertThat(actual.reviewId).isEqualTo(1)
        Assertions.assertThat(actual.review).isEqualTo("review")
    }

    private fun getMockReview(): Review {
        val review = Review()
        review.id = "id"
        review.review = "review"
        review.reviewId = 1
        return review
    }

    private fun getMockSearchHits(): List<SearchHit<Review>> {
        val review = getMockReview()
        val highLightMap = mutableMapOf("review" to listOf("<keyword>review</keyword>"))
        return listOf(SearchHit<Review>("mockId", 2.toFloat(), null, highLightMap, review))
    }

    private fun getMockUpdatedReviewDTO(): UpdateReviewDTO {
        val review = UpdateReviewDTO()
        review.review = "updated"
        return review
    }

    private fun <T> anyObject(): T {
        return Mockito.anyObject()
    }

}