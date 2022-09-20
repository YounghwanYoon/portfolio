package com.example.portfolio.feature_weather.domain.use_case

import android.util.Log
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.utils.DataState
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import com.example.portfolio.feature_weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWeatherInfo(
    private val repository: WeatherRepository
) {
/*    suspend operator fun invoke(gps:Map<String, Int?>): Flow<DataState<Forecast>>{
        Log.d(TAG, "invoke: GetForecastInfo is called")
        if(gps.isEmpty())
            return flow{}
        return repository.getForecast(gps)
    }*/
    suspend operator fun invoke(gps:Map<String, Int?>): Flow<DataState<Weather>>{
        Log.d(TAG, "invoke: GetForecastInfo is called")
        if(gps.isEmpty())
            return flow{}
        return repository.getWeather(gps)
    }

    suspend fun getForecast(gridId:String,gridX:Int,gridY:Int):Flow<DataState<Forecast>>{
        return repository.getForecast(gridId,gridX,gridY)
    }

    suspend fun getForecastHourly(gridId:String,gridX:Int,gridY:Int):Flow<DataState<ForecastHourly>>{
        return repository.getForecastHourly(gridId,gridX,gridY)
    }

    companion object {
        private const val TAG = "GetForecastInfo"
    }
}