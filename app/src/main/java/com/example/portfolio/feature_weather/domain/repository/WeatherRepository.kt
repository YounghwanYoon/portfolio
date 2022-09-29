package com.example.portfolio.feature_weather.domain.repository

import com.example.portfolio.utils.DataState
import com.example.portfolio.feature_weather.data.remote.dto.WeatherDto
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeather(gpsData:Map<String, Int?>): Flow<DataState<Weather>>
    fun getForecast(gridId:String,gridX:Int,gridY:Int):Flow<DataState<Forecast>>
    fun getForecastHourly(gridId:String,gridX:Int,gridY:Int):Flow<DataState<ForecastHourly>>

}