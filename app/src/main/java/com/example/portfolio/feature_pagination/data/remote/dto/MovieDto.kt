package com.example.portfolio.feature_pagination.data.remote.dto

import com.example.portfolio.feature_pagination.data.local.entity.MovieEntity

data class MovieDto(
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
){
    fun toMovieEntity(): MovieEntity {
        return MovieEntity(
            id = this.id,
            remote_id = this.id,
            original_language = this.original_language,
            original_title = this.original_title,
            overview = this.overview,
            popularity = this.popularity,
            poster_path = this.poster_path,
            release_date = this.release_date,
            title = this.title,
            video = this.video,
            vote_average = this.vote_average,
            vote_count = this.vote_count,
            page = 1,
        )
    }
}