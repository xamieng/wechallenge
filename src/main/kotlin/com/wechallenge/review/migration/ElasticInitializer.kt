package com.wechallenge.review.migration

import com.wechallenge.review.domain.Review
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
        private val reviewRepository: ReviewRepository
) {
    private val logger = LoggerFactory.getLogger(ElasticInitializer::class.java)

    @PostConstruct
    fun postConstruct() {
        val startTime = System.currentTimeMillis()
        var numberOfRecord = 0
        logger.info("Start migrating review data")
        val isAlreadyMigrated = reviewRepository.findOneByReviewId(1) != null
        if (!isAlreadyMigrated) {
            val reviewDataSet = executeMigration()
            numberOfRecord = reviewDataSet.size
            reviewRepository.saveAll(reviewDataSet)
        }
        logger.info("Finish migrating review data: Time=${System.currentTimeMillis() - startTime}, Record=${numberOfRecord}")
    }

    fun executeMigration(): MutableSet<Review> {
        var line: String?
        var fileReader: ReversedLinesFileReader? = null
        try {
            fileReader = ReversedLinesFileReader(File("/opt/app/migration.csv"), Charset.defaultCharset())
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
            println("Reading CSV Error!")
            e.printStackTrace()
        } finally {
            try {
                fileReader?.close()
            } catch (e: IOException) {
                println("Closing fileReader Error!")
                e.printStackTrace()
            }
        }
        return mutableSetOf()
    }
}