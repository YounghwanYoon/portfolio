package com.example.portfolio.feature_pagination.data.remote.service

import com.example.portfolio.feature_pagination.data.remote.dto.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    //https://api.themoviedb.org/3/movie/top_rated?api_key=228e77b34046660519f4ba0157db99b1
    //http://api.themoviedb.org/3/movie/top_rated?api_key=228e77b34046660519f4ba0157db99b1&page=2
    @GET("top_rated/")
    suspend fun getTopRateMovies(
        @Query("api_key") key:String,
        @Query("language") language:String,
        @Query("page") page:Int
    ): MovieResponse

    @GET("movie/top_rated?api_key=228e77b34046660519f4ba0157db99b1&language=en-US")
    suspend fun getTopRateMoviesTBDB(
        //@Query("api_key") key:String,
        //@Query("language") language:String,
        @Query("page") page:Int
    ): MovieResponse

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }
    //in order to load image
    //https://image.tmdb.org/t/p/original/wwemzKWzjKYJFfCeiB57q3r4Bcm.svg
//
}