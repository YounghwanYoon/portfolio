package com.example.portfolio.feature_pagination.data.remote.dto

import com.google.gson.annotations.SerializedName


data class MovieResponse(
    val page:Int,
    @SerializedName("results")
    val movies:List<MovieDto>,
    val total_pages:Int,
    val total_results:Int,
)

