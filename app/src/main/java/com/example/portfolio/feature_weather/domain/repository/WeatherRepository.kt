package com.example.portfolio.feature_weather.domain.repository

import com.example.portfolio.utils.DataState
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import kotlinx.coroutines.flow.Flow

/**
 * WeatherRepository will fetch and cache data from API, then return to caller.
 */
interface WeatherRepository {

    /**
     * Calling this method to calculated Grid X and Y Information which need to
     * get weather forecast data from server
     *
     * @param gpsData Map<String, Int?>
     * @return Flow<DataState<Weather>>
     */
    fun getWeather(gpsData:Map<String, Int?>): Flow<DataState<Weather>>

    /**
     * Calling this will receive daily forecast information of given Grid X and Y
     *
     * @param gridId String
     * @param gridX Int
     * @param gridY Int
     * @return Flow<DataState<Forecast>>
     */
    fun getWeeklyForecast(gridId:String, gridX:Int, gridY:Int):Flow<DataState<Forecast>>

    /**
     * Calling this will receive hourly forecast information of given Grid X and Y
     * @param gridId String
     * @param gridX Int
     * @param gridY Int
     * @return Flow<DataState<ForecastHourly>>
     */
    fun getForecastHourly(gridId:String,gridX:Int,gridY:Int):Flow<DataState<ForecastHourly>>

}