package com.example.portfolio.feature_weather.domain.repository

import com.example.portfolio.feature_weather.data.remote.dto.WeatherDto
import com.example.portfolio.feature_weather.data.remote.dto.forecast_dto.ForecastDto
import com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto.ForecastHourlyDto
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherServices {

    //https://api.weather.gov/points/37.422,-122.084
    /*@GET("/points/{latitude},{longitude}")
    suspend fun getWeather(@Query("latitude")latitude:String, @Query("longitude") longitude:String): Call<Weather>  //https://api.weather.gov/points/37.422,-122.084
    */
    @GET("/points/{latitude},{longitude}")
    suspend fun getWeather(@Path("latitude")latitude:Int, @Path("longitude") longitude:Int): Response<WeatherDto>

    //https://api.weather.gov/gridpoints/TOP/31,80/forecast/hourly
    @GET("/gridpoints/{gridId}/{gridX},{gridY}/forecast")
    suspend fun getForecast(@Path("gridId") gridId:String, @Path("gridX")gridX:Int, @Path("gridY")gridY:Int) : Response<ForecastDto>

    @GET("/gridpoints/{gridId}/{gridX},{gridY}/forecast/hourly")
    suspend fun getForecastHourly(@Path("gridId") gridId:String, @Path("gridX")gridX:Int, @Path("gridY")gridY:Int) : Response<ForecastHourlyDto>

}