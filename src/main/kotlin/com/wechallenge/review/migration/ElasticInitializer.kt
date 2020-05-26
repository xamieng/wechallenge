package com.wechallenge.review.migration

import com.wechallenge.review.domain.Keyword
import com.wechallenge.review.domain.Review
import com.wechallenge.review.repository.KeywordRepository
import com.wechallenge.review.repository.ReviewRepository
import org.apache.commons.io.input.ReversedLinesFileReader
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import javax.annotation.PostConstruct

@Component
@Transactional
class ElasticInitializer(
        private val reviewRepository: ReviewRepository,
        private val keywordRepository: KeywordRepository
) {
    private val logger = LoggerFactory.getLogger(ElasticInitializer::class.java)

    @PostConstruct
    fun initialData() {
        val startTime = System.currentTimeMillis()
        logger.info("Start migrating data")
        val isAlreadyMigrated = reviewRepository.findOneByReviewId(1) != null
        var reviewDataSet = mutableSetOf<Review>()
        var keywordDataSet = mutableSetOf<Keyword>()
        if (!isAlreadyMigrated) {
            reviewDataSet = readReviewDataFile()
            keywordDataSet = readKeywordDataFile()
            reviewRepository.saveAll(reviewDataSet)
            keywordRepository.saveAll(keywordDataSet)
        }
        logger.info("Finish migrating data: " +
                "Time=${System.currentTimeMillis() - startTime}, " +
                "Review=${reviewDataSet.size} " +
                "Keyword=${keywordDataSet.size}")
    }

    fun readReviewDataFile(): MutableSet<Review> {
        var line: String?
        var fileReader: ReversedLinesFileReader? = null
        try {
            val filePath = System.getenv("REVIEW_PATH") ?: ElasticInitializer::class.java.getResource("review.csv").readText()
            fileReader = ReversedLinesFileReader(File(filePath), Charset.defaultCharset())
            val reviewDataSet = mutableSetOf<Review>()
            var reviewText = ""
            line = fileReader.readLine()
            while (line != null) {
                val tokens = line.split(";")
                if (tokens.isNotEmpty()) {
                    if (tokens.size == 2) {
                        try {
                            val reviewId = tokens[0].toInt()
                            val r = Review()
                            r.review = tokens[1] + reviewText
                            r.reviewId = reviewId
                            reviewDataSet.add(r)
                            reviewText = ""
                        } catch (e: Exception) {
                            reviewText = tokens[0] + reviewText
                        }
                    } else {
                        reviewText = tokens[0] + reviewText
                    }
                }
                line = fileReader.readLine()
            }
            return reviewDataSet
        } catch (e: Exception) {
            println("Reading review file Error!")
            e.printStackTrace()
        } finally {
            try {
                fileReader?.close()
            } catch (e: IOException) {
                println("Closing review file Error!")
                e.printStackTrace()
            }
        }
        return mutableSetOf()
    }

    fun readKeywordDataFile(): MutableSet<Keyword> {
        var line: String?
        var fileReader: ReversedLinesFileReader? = null
        try {
            val filePath = System.getenv("KEYWORD_PATH") ?: ElasticInitializer::class.java.getResource("keyword.txt").readText()
            fileReader = ReversedLinesFileReader(File(filePath), Charset.defaultCharset())
            val keywordDataSet = mutableSetOf<Keyword>()
            line = fileReader.readLine()
            while (line != null) {
                if (!line.isNullOrBlank()) {
                    val keyword = Keyword()
                    keyword.keyword = line
                    keywordDataSet.add(keyword)
                }
                line = fileReader.readLine()
            }
            return keywordDataSet
        } catch (e: Exception) {
            println("Reading keyword file Error!")
            e.printStackTrace()
        } finally {
            try {
                fileReader?.close()
            } catch (e: IOException) {
                println("Closing keyword file Error!")
                e.printStackTrace()
            }
        }
        return mutableSetOf()
    }

}