package com.example.portfolio.feature_pagination.data.remote.service

import com.example.portfolio.feature_pagination.data.remote.dto.MovieData
import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import com.example.portfolio.feature_pagination.domain.model.util.ResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    //https://api.themoviedb.org/3/movie/top_rated?api_key=228e77b34046660519f4ba0157db99b1
    //http://api.themoviedb.org/3/movie/top_rated?api_key=228e77b34046660519f4ba0157db99b1&page=2
    @GET("movie/top_rated")
    suspend fun getTopRateMovies(
        @Query("api_key") key:String,
        @Query("language") language:String,
        @Query("page") page:Int
    ): MovieData

    /*@GET("movie/top_rated")
    suspend fun getTopRateMovies(
        @Query("api_key") key:String,
        @Query("language") language:String,
        @Query("page") page:Int
    ): List<MovieResponse>*/
/*
    @GET("movie/top_rated")
    suspend fun getTopRateMovies(
        @Query("api_key") key:String,
        @Query("language") language:String,
        @Query("page") page:Int
    ): ResponseItem<MovieResponse>
*/

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }
    //in order to load image
    //https://image.tmdb.org/t/p/original/wwemzKWzjKYJFfCeiB57q3r4Bcm.svg
//
}