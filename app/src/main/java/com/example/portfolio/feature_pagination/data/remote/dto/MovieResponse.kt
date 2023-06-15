package com.example.portfolio.feature_pagination.data.remote.dto

import com.example.portfolio.feature_pagination.domain.model.Movie
import com.example.portfolio.feature_pagination.domain.model.Result

data class MovieResponse(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
){
    fun toMovie(): Movie {
        return Movie(
            page = this.page,
            results = this.results,
            total_pages = this.total_pages,
            total_results = this.total_results
        )
    }
}