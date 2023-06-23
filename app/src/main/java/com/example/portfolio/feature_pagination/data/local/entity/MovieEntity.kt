package com.example.portfolio.feature_pagination.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.portfolio.feature_pagination.domain.model.Movie

@Entity (tableName = "movie_entity")
data class MovieEntity(
    @PrimaryKey
    val id:Int,
    val remote_id:Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    @ColumnInfo(name = "page")
    var page: Int,
)
fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = this.id,
        //original_language = this.original_language,
        original_title = this.original_title,
        overview = this.overview,
        //popularity = this.popularity,
        poster_path = this.poster_path,
        release_date = this.release_date,
        //title = this.title,
        //video = this.video,
        vote_average = this.vote_average,
        vote_count = this.vote_count
    )
}