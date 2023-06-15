package com.example.portfolio.feature_pagination.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.portfolio.feature_pagination.domain.model.Movie
import com.example.portfolio.feature_pagination.domain.model.Result

@Entity
data class MovieEntity(
    @PrimaryKey
    val id:Int,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)
