package com.example.portfolio.feature_pagination3.data.remote

import com.example.portfolio.feature_pagination3.data.remote.dto.BeerDto
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerApi {
//https://api.punkapi.com/v2/beers?page=1&per_page=20
    //https://api.punkapi.com/v2/beers?page=2&per_page=80
    @GET("beers")
    suspend fun getBeers(
        @Query("page") page:Int,
        @Query("per_page") pageCount:Int
    ):List<BeerDto>

    companion object{
        //https://punkapi.com/documentation/v2
        const val BASE_URL:String = "https://api.punkapi.com/v2/"
    }
}