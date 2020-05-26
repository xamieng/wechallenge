package com.wechallenge.review.domain

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "wechallenge", type = "keyword")
class Keyword {
    @Id
    var id: String? = null
    var keyword: String? = null
    override fun toString() = "Keyword: { id=$id, keyword=$keyword }"
}