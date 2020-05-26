package com.wechallenge.review.repository

import com.wechallenge.review.domain.Keyword
import com.wechallenge.review.domain.Review
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface KeywordRepository: ElasticsearchRepository<Keyword, String> {
    fun findOneByKeyword(keyword: String): Keyword?
}