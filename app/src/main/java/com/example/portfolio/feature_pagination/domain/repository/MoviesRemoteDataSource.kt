package com.example.portfolio.feature_pagination.domain.repository

import androidx.paging.PagingData
import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteDataSource{

    fun getMovies(): Flow<PagingData<MovieResponse>>
}