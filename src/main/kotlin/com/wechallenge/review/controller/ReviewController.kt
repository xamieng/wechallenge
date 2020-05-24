package com.wechallenge.review.controller

import com.wechallenge.review.assembler.ReviewAssembler
import com.wechallenge.review.dto.ReviewDTO
import com.wechallenge.review.dto.UpdateReviewDTO
import com.wechallenge.review.service.ReviewService
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@CrossOrigin(origins = ["http://localhost:5555"])
@RequestMapping("/reviews")
class ReviewController(
        private val reviewService: ReviewService,
        private val reviewAssembler: ReviewAssembler
) {

    private val logger = LoggerFactory.getLogger(ReviewController::class.java)

    @GetMapping("/{id}")
    fun getReviewById(@PathVariable id: Int): ResponseEntity<ReviewDTO> {
        logger.info("REST request to get review by id: $id")
        val review = reviewService.getReviewById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val reviewDto = reviewAssembler.assembleDTO(review)
        return ResponseEntity.ok(reviewDto)
    }

    @GetMapping
    @ApiImplicitParams(
            ApiImplicitParam(name = "sort", value = "Field to sort,{asc, desc}", defaultValue = "id,asc", paramType = "query", dataType = "string"),
            ApiImplicitParam(name = "size", value = "Page Size", defaultValue = "100", paramType = "query", dataType = "int"),
            ApiImplicitParam(name = "page", value = "Page number", defaultValue = "0", paramType = "query", dataType = "int")
    )
    fun searchReviewByFoodText(
            @RequestParam("query") query: String,
            @ApiIgnore pageable: Pageable): ResponseEntity<List<ReviewDTO>> {
        logger.info("REST request to search review by food text: $query")
        val reviews = reviewService.searchReviewByFoodText(query, pageable)
        val reviewDtos = reviews.map { reviewAssembler.assembleDTO(it) }
        return ResponseEntity.ok(reviewDtos)
    }

    @PutMapping("/{id}")
    fun updateReviewByReviewId(
            @PathVariable id: Int,
            @RequestBody dto: UpdateReviewDTO): ResponseEntity<ReviewDTO> {
        logger.info("REST request to update review by id: $id")
        val review = reviewService.getReviewById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val updatedReview = reviewService.updateReview(review, dto)
        val reviewDto = reviewAssembler.assembleDTO(updatedReview)
        return ResponseEntity.ok(reviewDto)
    }
}