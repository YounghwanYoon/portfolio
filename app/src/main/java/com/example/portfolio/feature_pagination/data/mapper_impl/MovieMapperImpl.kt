package com.example.portfolio.feature_pagination.data.mapper_impl

import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import com.example.portfolio.feature_pagination.domain.model.Movie
import com.example.portfolio.feature_pagination.domain.model.util.MovieMapper
import com.example.portfolio.feature_pagination.presentation.ui.MovieUi

class MovieMapperImpl:MovieMapper {
    override suspend fun mapRemoteMoviesListToDomain(remoteMovies: List<MovieResponse>): List<Movie> {
        return remoteMovies.map{
            mapRemoteMovieToDomain(it)
        }
    }

    override suspend fun mapRemoteMovieToDomain(remoteMovie: MovieResponse): Movie {
        return Movie(
            page = remoteMovie.page,
            results = remoteMovie.results,
            total_pages = remoteMovie.total_pages,
            total_results = remoteMovie.total_results
        )
    }

    override suspend fun mapDomainMovieListToUi(domainMovie: List<Movie>): List<MovieUi> {
        TODO("Not yet implemented")
    }

    override suspend fun mapDomainMovieToUi(domainMovie: Movie): MovieUi {
        return MovieUi(
            page = domainMovie.page,
            results = domainMovie.results,
            total_pages = domainMovie.total_pages,
            total_results = domainMovie.total_results
        )
    }
}