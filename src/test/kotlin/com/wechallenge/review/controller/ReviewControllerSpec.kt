package com.wechallenge.review.controller

import com.wechallenge.review.assembler.ReviewAssembler
import com.wechallenge.review.domain.Review
import com.wechallenge.review.dto.UpdateReviewDTO
import com.wechallenge.review.service.ReviewService
import org.assertj.core.api.Assertions.assertThat
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
import org.springframework.http.HttpStatus

@Suppress("DEPRECATION")
@RunWith(MockitoJUnitRunner::class)
class ReviewControllerSpec {
    @Mock
    lateinit var reviewService: ReviewService
    var reviewAssembler = ReviewAssembler()
    lateinit var reviewController: ReviewController

    @Before
    fun setup() {
        reviewController = ReviewController(reviewService, reviewAssembler)
    }

    @Test
    fun testGetReviewById_when_success() {
        `when`(reviewService.getReviewById(ArgumentMatchers.anyInt())).thenReturn(getMockReview())

        val actual = reviewController.getReviewById(1)
        val actualBody = actual.body

        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)
        actualBody?.let {
            assertThat(it.review).isEqualTo("review")
            assertThat(it.reviewId).isEqualTo(1)
        }
    }

    @Test
    fun testGetReviewById_when_model_not_found() {
        `when`(reviewService.getReviewById(ArgumentMatchers.anyInt())).thenReturn(null)

        val actual = reviewController.getReviewById(1)

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }


    @Test
    fun testSearchReviewByFoodText_when_success() {
        `when`(reviewService.searchReviewByFoodText("test", Pageable.unpaged()))
                .thenReturn(getMockSearchHits())

        val actual = reviewController.searchReviewByFoodText("test", Pageable.unpaged())
        val actualBody = actual.body

        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)
        actualBody?.let {
            assertThat(it.first().review).isEqualTo("<keyword>review</keyword>")
            assertThat(it.first().reviewId).isEqualTo(1)
        }
    }

    @Test
    fun testSearchReviewByFoodText_when_not_found() {
        `when`(reviewService.searchReviewByFoodText("test", Pageable.unpaged()))
                .thenReturn(listOf())

        val actual = reviewController.searchReviewByFoodText("test", Pageable.unpaged())
        val actualBody = actual.body

        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)
        actualBody?.let {
            assertThat(it.size).isEqualTo(0)
        }
    }

    @Test
    fun testUpdateReviewByReviewId_when_success() {
        `when`(reviewService.getReviewById(ArgumentMatchers.anyInt())).thenReturn(getMockReview())
        `when`(reviewService.updateReview(anyObject(), anyObject())).thenReturn(getMockUpdatedReview())

        val actual = reviewController.updateReviewByReviewId(1, getMockUpdatedReviewDTO())
        val actualBody = actual.body

        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)
        actualBody?.let {
            assertThat(it.review).isEqualTo("updated")
            assertThat(it.reviewId).isEqualTo(1)
        }
    }

    @Test
    fun testUpdateReviewByReviewId_when_not_found() {
        `when`(reviewService.getReviewById(ArgumentMatchers.anyInt())).thenReturn(null)

        val actual = reviewController.updateReviewByReviewId(1, getMockUpdatedReviewDTO())

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    private fun getMockSearchHits(): List<SearchHit<Review>> {
        val review = getMockReview()
        val highLightMap = mutableMapOf("review" to listOf("<keyword>review</keyword>"))
        return listOf(SearchHit<Review>("mockId", 2.toFloat(), null, highLightMap, review))
    }

    private fun getMockReview(): Review {
        val review = Review()
        review.id = "id"
        review.review = "review"
        review.reviewId = 1
        return review
    }

    private fun getMockUpdatedReviewDTO(): UpdateReviewDTO {
        val review = UpdateReviewDTO()
        review.review = "updated"
        return review
    }

    private fun getMockUpdatedReview(): Review {
        val review = Review()
        review.id = "id"
        review.reviewId = 1
        review.review = "updated"
        return review
    }

    private fun <T> anyObject(): T {
        return Mockito.anyObject()
    }
}