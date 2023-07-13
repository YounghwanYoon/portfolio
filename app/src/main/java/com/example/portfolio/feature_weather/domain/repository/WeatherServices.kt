package com.example.portfolio.feature_weather.domain.repository

import com.example.portfolio.feature_weather.data.remote.dto.WeatherDto
import com.example.portfolio.feature_weather.data.remote.dto.forecast_dto.ForecastDTO
import com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto.ForecastHourlyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherServices {

    //https://api.weather.gov/points/37.422,-122.084
    /*@GET("/points/{latitude},{longitude}")
    suspend fun getWeather(@Query("latitude")latitude:String, @Query("longitude") longitude:String): Call<Weather>  //https://api.weather.gov/points/37.422,-122.084
    */

    /**
     * With lattitude and longitude received from User,
     * it will calculate and provide grid X and Y which
     * then we can use to get forecast information of specific zone.
     *
     * @param latitude Int
     * @param longitude Int
     * @return Response<WeatherDto>
     */
    @GET("/points/{latitude},{longitude}")
    suspend fun getWeather(@Path("latitude")latitude:Int, @Path("longitude") longitude:Int): Response<WeatherDto>

    /**
     * Based on the grid X and Y information, received from getWeather(), server will response back
     * with forecast information
     *
     * @param gridId String
     * @param gridX Int
     * @param gridY Int
     * @return Response<ForecastDto>
     */
    // * https://api.weather.gov/gridpoints/TOP/31,80/forecast/hourly
    @GET("/gridpoints/{gridId}/{gridX},{gridY}/forecast")
    suspend fun getWeeklyForecast(@Path("gridId") gridId:String, @Path("gridX")gridX:Int, @Path("gridY")gridY:Int) : Response<ForecastDTO>

    /**
     * Based on the grid X and Y information, received from getWeather(), server will response back
     * with hourly forecast data
     * @param gridId String
     * @param gridX Int
     * @param gridY Int
     * @return Response<ForecastHourlyDto>
     */
    @GET("/gridpoints/{gridId}/{gridX},{gridY}/forecast/hourly")
    suspend fun getForecastHourly(@Path("gridId") gridId:String, @Path("gridX")gridX:Int, @Path("gridY")gridY:Int) : Response<ForecastHourlyDto>

}