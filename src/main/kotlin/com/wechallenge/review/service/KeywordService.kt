package com.wechallenge.review.service

import com.wechallenge.review.domain.Keyword
import com.wechallenge.review.repository.KeywordRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class KeywordService (
        private val keywordRepository: KeywordRepository
) {

    private val logger = LoggerFactory.getLogger(KeywordService::class.java)

    @Transactional(readOnly = true)
    @Cacheable("keyword")
    fun getKeyword(keyword: String): Keyword? {
        logger.debug("getKeyword: $keyword")
        return keywordRepository.findOneByKeyword(keyword)
    }
}