package com.example.portfolio.feature_weather.data.repository

import android.util.Log
import com.example.portfolio.feature_weather.data.local.WeatherDao
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDao
import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.ForecastHourlyDao
import com.example.portfolio.feature_weather.data.remote.dto.WeatherDto
import com.example.portfolio.feature_weather.data.remote.dto.forecast_dto.ForecastDto
import com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto.ForecastHourlyDto
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import com.example.portfolio.feature_weather.domain.repository.WeatherRepository
import com.example.portfolio.feature_weather.domain.repository.WeatherServices
import com.example.portfolio.utils.DataState
import com.example.portfolio.utils.NetworkError
import com.example.portfolio.utils.ServerSideError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherServices: WeatherServices,
    private val weather_dao: WeatherDao? = null,
    private val forecast_dao: ForecastDao,
    private val forecastHourly_dao: ForecastHourlyDao,
    private val ioDispatcher:CoroutineDispatcher = Dispatchers.IO
    ): WeatherRepository{

    override fun getWeather(gpsData:Map<String, Int?>):Flow<DataState<Weather>>{
        Log.d(TAG, "getWeather: getWeather is called from repository implement with gps${gpsData["latitude"]!!} ${gpsData["longitude"]}")
        return flow<DataState<Weather>>{
            this.emit(DataState.Loading())
            var body: WeatherDto? = null
            val response = weatherServices.getWeather(gpsData["latitude"]!!, gpsData["longitude"]!!)
            if(response.code() == 500){
                Log.d(TAG, "getWeather: Error code 500 alert!")
                throw ServerSideError("ServerSide Error with code 500")
            }
            try{
                Log.d(TAG, "getWeather: response code ${response.code()}")
                body = response.body()
                if(response.isSuccessful && body != null){
                    val weather = body.toWeather()
                   emit(DataState.Success(weather))
                }
            }catch (e:Exception){
                Log.d(TAG, "getWeather: something went off")
                emit(returnErrorByType(response.code(), e))
            }
        }
        .retry(2) { exception ->
            Log.d(TAG, "getWeather: retrying from weatherRespository")
            (exception is ServerSideError).also {
                if(it) delay(1000)
            }
        }
        .catch{ exception ->
            emit(DataState.Error(exception.message ?:"Unknown Error from repository-getWeather()"))
        }
        .flowOn(ioDispatcher)
    }


    override fun getForecast(gridId:String,gridX:Int,gridY:Int): Flow<DataState<Forecast>> {
        Log.d(TAG, "getForecast: getForecast is called from repository implement")
        return flow<DataState<Forecast>>{
            emit(DataState.Loading())

            var data:ForecastDto? = null
/*
            //if forecast_dao is not null, then return Loading state with previously stored data.
            val forecast : ForecastEntity? = forecast_dao.getAllForecast()
            forecast?.let{
                Log.d(TAG, "getForecast: hey room is not emptied")
                emit(DataState.Loading(it.toForecast()))
            }

 */

            val response = weatherServices.getForecast(gridId, gridX, gridY)
            if(response.code() == 500){
                Log.d(TAG, "getWeather: Error code 500 alert!")
                throw ServerSideError("ServerSide Error with code 500")
            }
            try {
                Log.d(TAG, "getForecast: code ${response.code()}")
                if(response.isSuccessful){
                    data = response.body()
                    delay(1000)
                    emit(DataState.Success(data?.toForecastEntity()?.toForecast()))

                    //data = response.body()
                    Log.d(TAG, "getForecast: success with DTO with updateTime of ${data!!.propertiesDto.updateTime}")
                }
                //store to local data ...causing so much error
                //it kept saying data is null when trying to storing it to room.
                //then it said not null when returning directly to main thread...hm.
                //possible suspending /delaying thread
                data?.let{
                    forecast_dao.insertForecast(it.toForecastEntity())
                }
            }catch (e:Exception) {
                Log.d(TAG, "getForecast: Something went wrong")
                emit(returnErrorByType(response.code(), e))
            }
//s            Log.d(TAG, "getForecast: ${forecast_dao.getAllForecast().properties.elevation}")
            //val forecast = forecast_dao.getAllForecast().toForecast()
            //emit(DataState.Success(data?.toForecastEntity()?.toForecast()))
        }
            .retry(2) { exception ->
                Log.d(TAG, "getForecast: serverside error and retrying from repository")
                (exception is ServerSideError).also{if(it) delay(1000)}
            }
            .catch { exception ->
                Log.d(TAG, "getForecast: inside catch block ${exception.cause}")
                emit(DataState.Error(exception.message ?: "Unknown Error from repository-getForecast()"))
            }
            .flowOn(ioDispatcher)
    }
    override fun getForecastHourly(gridId:String,gridX:Int,gridY:Int): Flow<DataState<ForecastHourly>> {
        Log.d(TAG, "getForecastHourly: getForecastHourly is called from repository implement")
        return flow {
            emit(DataState.Loading())

            var data: ForecastHourlyDto? = null

            //If forecasthourly_dao is not null, return Loading state with stored data.

            val forecastHEntity = forecastHourly_dao.getAllForecastHourly()
            forecastHEntity?.let {
                Log.d(TAG, "getForecastHourly: hey FHEntity is not emptied")
                emit(DataState.Loading(it.toForecastHourly()))
            }
            val response =
                weatherServices.getForecastHourly(gridId, gridX, gridY)
            if(response.code() == 500){
                Log.d(TAG, "getWeather: Error code 500 alert!")
                throw ServerSideError("ServerSide Error with code 500")
            }
            try {
//                val response =
//                    weatherServices.getForecastHourly(gridId, gridX, gridY)
                if (response.isSuccessful) {
                    data = response.body()
                }
                data?.let {
                    forecastHourly_dao.insertForecastHourly(it.toForecastHourlyEntity())
                }

            } catch (e: Exception) {
                Log.d(TAG, "getForecastHourly: Something went wrong")
                emit(returnErrorByType(response.code(), e))
            }
            delay(1000)

            emit(DataState.Success(data?.toForecastHourlyEntity()?.toForecastHourly()))

        }
            .retry(2){ exception ->
                (exception is ServerSideError).also{
                    Log.d(TAG, "getForecastHourly: serverside error and need to do it again")
                    if(it) delay(1000)
                }
            }
            .catch { exception -> Log.d(TAG, "getForecastHourly: inside catch block ${exception.cause}") }
            .flowOn(ioDispatcher)
    }
    private suspend fun insertForecast(data:ForecastDto){
        forecast_dao.insertForecast(data.toForecastEntity())
    }

    private suspend fun insertForecastHourly(data:ForecastHourlyDto){
        forecastHourly_dao.insertForecastHourly(data.toForecastHourlyEntity())
    }


    private fun <T> returnErrorByType(errorCode:Int, e:Exception):DataState.Error<T>{
        var message:String =""
        return when(errorCode){
            500 -> {
                message = "Server Side Error - ${e.message}"
                DataState.Error(message, errorCode = 500, errorType = NetworkError.ServerSideError)
            }
            502 ->{
                message = "InternetConnection Error - ${e.message}"
                DataState.Error(message, errorCode = 502, errorType = NetworkError.InternetConnectionError)
            }
            else ->{
                message = "PageNotFound Error - ${e.message}"
                DataState.Error(message, errorCode = 400, errorType = NetworkError.PageNotFoundError)
            }
        }
    }

    companion object {
        private const val TAG = "WeatherRepositoryImpl"
    }

}