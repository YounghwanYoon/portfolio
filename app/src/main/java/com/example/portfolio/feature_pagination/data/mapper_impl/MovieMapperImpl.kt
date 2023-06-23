package com.example.portfolio.feature_pagination.data.mapper_impl

import com.example.portfolio.feature_pagination.data.local.entity.MovieEntity
import com.example.portfolio.feature_pagination.data.remote.dto.MovieDto
import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import com.example.portfolio.feature_pagination.domain.model.Movie
import com.example.portfolio.feature_pagination.domain.model.util.MovieMapper

class MovieMapperImpl:MovieMapper {
    override suspend fun mapRemoteMoviesListToDomain(remoteMovies: List<MovieDto>): List<MovieEntity> {
        return remoteMovies.map{mapRemoteMovieToDomain(it)}
    }

    override suspend fun mapRemoteMovieToDomain(remoteMovie: MovieDto): MovieEntity {
        return MovieEntity(
            id = remoteMovie.id,
            remote_id = remoteMovie.id,
            original_language = remoteMovie.original_language,
            original_title = remoteMovie.original_title,
            overview = remoteMovie.overview,
            popularity = remoteMovie.popularity,
            poster_path = remoteMovie.poster_path,
            release_date = remoteMovie.release_date,
            title = remoteMovie.title,
            video = remoteMovie.video,
            vote_average = remoteMovie.vote_average,
            vote_count = remoteMovie.vote_count,
            page = 1

        )
    }

    override suspend fun mapDomainMovieListToUi(domainMovie: List<MovieEntity>): List<Movie> {
        return domainMovie.map{mapDomainMovieToUi(it)}
    }

    override suspend fun mapDomainMovieToUi(domainMovie: MovieEntity): Movie {
        return Movie(
            id = domainMovie.id,
            //remote_id = domainMovie.id,
            //original_language = domainMovie.original_language,
            original_title = domainMovie.original_title,
            overview = domainMovie.overview,
            //popularity = domainMovie.popularity,
            poster_path = domainMovie.poster_path,
            release_date = domainMovie.release_date,
            //title = domainMovie.title,
            //video = domainMovie.video,
            vote_average = domainMovie.vote_average,
            vote_count = domainMovie.vote_count,
        )
    }

}