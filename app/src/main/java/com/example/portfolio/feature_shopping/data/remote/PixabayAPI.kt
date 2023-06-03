package com.example.portfolio.feature_shopping.data.remote

import com.example.portfolio.feature_shopping.data.remote.dto.PixabayDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PixabayAPI {

    //https://pixabay.com/api/?key={ KEY }&q=yellow+flowers&image_type=photo
/*    @GET("/?key={KEY}&q={SEARCH}&image_type=photo")
    fun getPixaBaySeasonHolidayData(@Path("KEY") key:String, @Query("SEARCH") search:String): Response<PixabayDTO>*/

    @GET("/api")
    suspend fun getPixaBaySeasonHolidayData(
        @Query("key") key:String,
        @Query("q") search:String,
        @Query("image_type") imageType:String = "photo"
    ): Response<PixabayDTO>

    //https://pixabay.com/api/?key=15227824-a2215005f70965bf20cc7de51&q=coffee+beans&image_type=photo

    @GET("/api")
    suspend fun getPixaBayCoffeeData(
        @Query("key") key:String,
        @Query("q") search:String,
        @Query("image_type") imageType:String = "photo"
    ): Response<PixabayDTO>


}