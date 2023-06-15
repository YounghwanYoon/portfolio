package com.example.portfolio.feature_pagination.presentation.ui

import android.os.Parcelable
import com.example.portfolio.feature_pagination.domain.model.Movie
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieUi(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
):Parcelable
