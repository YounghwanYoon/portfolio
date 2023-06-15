package com.example.portfolio.feature_pagination.domain.model.util

import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import com.example.portfolio.feature_pagination.domain.model.Movie
import com.example.portfolio.feature_pagination.presentation.ui.MovieUi

interface MovieMapper {

    suspend fun mapRemoteMoviesListToDomain(remoteMovies:List<MovieResponse>):List<Movie>
    suspend fun mapRemoteMovieToDomain(remoteMovie:MovieResponse): Movie

    suspend fun mapDomainMovieListToUi(domainMovie:List<Movie>): List<MovieUi>
    suspend fun mapDomainMovieToUi(domainMovie: Movie): MovieUi
}