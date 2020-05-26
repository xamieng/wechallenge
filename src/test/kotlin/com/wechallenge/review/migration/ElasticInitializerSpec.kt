package com.wechallenge.review.migration

import com.wechallenge.review.domain.Keyword
import com.wechallenge.review.domain.Review
import com.wechallenge.review.repository.KeywordRepository
import com.wechallenge.review.repository.ReviewRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@Suppress("DEPRECATION")
@RunWith(MockitoJUnitRunner::class)
class ElasticInitializerSpec {
    @Mock private lateinit var reviewRepository: ReviewRepository
    @Mock private lateinit var keywordRepository: KeywordRepository
    private lateinit var elasticInitializer: ElasticInitializer

    @Before
    fun setup() {
        elasticInitializer = ElasticInitializer(reviewRepository, keywordRepository)
    }

    @Test
    fun initialData_when_not_migrated_yet() {
        `when`(reviewRepository.findOneByReviewId(ArgumentMatchers.anyInt())).thenReturn(null)
        `when`(reviewRepository.saveAll(anyObject<MutableSet<Review>>())).thenReturn(emptyList())
        `when`(keywordRepository.saveAll(anyObject<MutableSet<Keyword>>())).thenReturn(emptyList())

        elasticInitializer.initialData()

        verify(reviewRepository, times(1)).findOneByReviewId(anyInt())
        verify(reviewRepository, times(1)).saveAll(anyObject<MutableSet<Review>>())
        verify(keywordRepository, times(1)).saveAll(anyObject<MutableSet<Keyword>>())
    }

    @Test
    fun initialData_when_already_migrated() {
        `when`(reviewRepository.findOneByReviewId(anyInt())).thenReturn(getMockReview())

        elasticInitializer.initialData()

        verify(reviewRepository, times(1)).findOneByReviewId(anyInt())
        verify(reviewRepository, times(0)).saveAll(anyObject<MutableSet<Review>>())
        verify(keywordRepository, times(0)).saveAll(anyObject<MutableSet<Keyword>>())
    }

    private fun getMockReview(): Review {
        val review = Review()
        review.id = "id"
        review.review = "review"
        review.reviewId = 1
        return review
    }

    private fun <T> anyObject(): T {
        return Mockito.anyObject()
    }
}