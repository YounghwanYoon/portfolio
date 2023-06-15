package com.example.portfolio.feature_pagination.data.repo_impl

import androidx.paging.PagingData
import androidx.paging.map
import com.example.portfolio.feature_pagination.domain.model.Movie
import com.example.portfolio.feature_pagination.domain.model.util.MovieMapper
import com.example.portfolio.feature_pagination.domain.repository.MoviesRemoteDataSource
import com.example.portfolio.feature_pagination.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
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