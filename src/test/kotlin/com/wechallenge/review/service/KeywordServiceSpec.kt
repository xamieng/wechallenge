package com.wechallenge.review.service

import com.wechallenge.review.domain.Keyword
import com.wechallenge.review.repository.KeywordRepository
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class KeywordServiceSpec {
    @Mock private lateinit var keywordRepository: KeywordRepository
    private lateinit var keywordService: KeywordService

    @Before
    fun setup() {
        keywordService = KeywordService(keywordRepository)
    }

    @Test
    fun testGetKeyword_when_success() {
        `when`(keywordRepository.findOneByKeyword(ArgumentMatchers.anyString())).thenReturn(getMockKeyword())

        val actual = keywordService.getKeyword("test")

        actual?.let {
            Assertions.assertThat(it.keyword).isEqualTo("test")
        }
    }

    @Test
    fun testGetReviewById_when_not_found() {
        `when`(keywordRepository.findOneByKeyword(ArgumentMatchers.anyString())).thenReturn(null)
        val actual = keywordService.getKeyword("test")
        Assertions.assertThat(actual).isEqualTo(null)
    }

    private fun getMockKeyword(): Keyword {
        val keyword = Keyword()
        keyword.keyword = "test"
        keyword.id = "mock"
        return keyword
    }
}