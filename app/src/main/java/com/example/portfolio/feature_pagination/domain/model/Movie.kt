package com.example.portfolio.feature_pagination.domain.model

data class Movie(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)