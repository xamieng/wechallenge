package com.wechallenge.review

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class ReviewApplication

fun main(args: Array<String>) {
	runApplication<ReviewApplication>(*args)
}
