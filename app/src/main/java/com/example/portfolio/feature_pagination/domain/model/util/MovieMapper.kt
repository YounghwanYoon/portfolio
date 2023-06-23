package com.example.portfolio.feature_pagination.domain.model.util

import com.example.portfolio.feature_pagination.data.local.entity.MovieEntity
import com.example.portfolio.feature_pagination.data.remote.dto.MovieDto
import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import com.example.portfolio.feature_pagination.domain.model.Movie

interface MovieMapper {

    suspend fun mapRemoteMoviesListToDomain(remoteMovies:List<MovieDto>):List<MovieEntity>
    suspend fun mapRemoteMovieToDomain(remoteMovie:MovieDto): MovieEntity

    suspend fun mapDomainMovieListToUi(domainMovie:List<MovieEntity>): List<Movie>
    suspend fun mapDomainMovieToUi(domainMovie: MovieEntity): Movie
}