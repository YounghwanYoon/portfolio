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
            id = 1262,
            original_language = "eius",
            original_title = "delenit",
            overview = "mutat",
            popularity = 8.9,
            poster_path = "non",
            release_date = "idque",
            title = "ubique",
            video = false,
            vote_average = 10.11,
            vote_count = 6134
            /*            page = remoteMovie.page,
                        results = remoteMovie.results,
                        total_pages = remoteMovie.total_pages,
                        total_results = remoteMovie.total_results*/
        
        )
    }

    override suspend fun mapDomainMovieListToUi(domainMovie: List<Movie>): List<MovieUi> {
        TODO("Not yet implemented")
    }

    override suspend fun mapDomainMovieToUi(domainMovie: Movie): MovieUi {
        return MovieUi(
            page = 7979, results = listOf(), total_pages = 5633, total_results = 3069

        )
    }
}