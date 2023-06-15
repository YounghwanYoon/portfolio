package com.example.portfolio.feature_pagination.domain.repository

import androidx.paging.PagingData
import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import com.example.portfolio.feature_pagination.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(): Flow<PagingData<Movie>>
}