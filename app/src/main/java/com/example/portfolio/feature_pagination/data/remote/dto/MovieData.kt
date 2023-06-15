package com.example.portfolio.feature_pagination.data.remote.dto


data class MovieData(
    val page:Int,
    val results:List<MovieResponse>,
    val total_pages:Int,
    val total_results:Int,
)
