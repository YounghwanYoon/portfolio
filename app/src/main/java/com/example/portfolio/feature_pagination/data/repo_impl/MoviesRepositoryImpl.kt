package com.example.portfolio.feature_pagination.data.repo_impl

import MoviesRemoteDataSourceImpl
import androidx.paging.PagingData
import androidx.paging.map
import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import com.example.portfolio.feature_pagination.data.remote.service.MovieService
import com.example.portfolio.feature_pagination.domain.model.Movie
import com.example.portfolio.feature_pagination.domain.model.util.MovieMapper
import com.example.portfolio.feature_pagination.domain.repository.MoviesRemoteDataSource
import com.example.portfolio.feature_pagination.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepositoryImpl(
    private val moviesRemoteDataSourceImpl: MoviesRemoteDataSource,
    private val mapper: MovieMapper
): MoviesRepository {

    override fun getMovies(): Flow<PagingData<Movie>> {
        return moviesRemoteDataSourceImpl.getMovies()
            .map{ pagingData ->
                pagingData.map{ remoteMovie ->
                    mapper.mapRemoteMovieToDomain(remoteMovie = remoteMovie)
                }
            }
    }
}