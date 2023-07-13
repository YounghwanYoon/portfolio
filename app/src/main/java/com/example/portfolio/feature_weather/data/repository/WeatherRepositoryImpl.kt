package com.example.portfolio.feature_weather.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.room.withTransaction
import com.example.portfolio.feature_weather.data.local.WeatherDao
import com.example.portfolio.feature_weather.data.local.WeatherDataBase
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDao
import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.ForecastHourlyDao
import com.example.portfolio.feature_weather.data.remote.dto.WeatherDto
import com.example.portfolio.feature_weather.data.remote.dto.forecast_dto.ForecastDTO
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
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("LogNotTimber")
class WeatherRepositoryImpl @Inject constructor(
    private val weatherServices: WeatherServices,
    private val weather_dao: WeatherDao? = null,
    private val forecast_dao: ForecastDao,
    private val weatherDataBase: WeatherDataBase,
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
    override fun getWeeklyForecast(gridId:String, gridX:Int, gridY:Int): Flow<DataState<Forecast>> {
        Log.d(TAG, "getWeeklyForecast: getWeeklyForecast is called from repository implement")
        return flow<DataState<Forecast>>{

            val cachedData = forecast_dao.getAllForecast()

            if(cachedData != null){
                Timber.tag(TAG).d("cached is found")
                val cachedForecast = cachedData.toForecast()
                emit(DataState.Loading(cachedForecast))
            }else{
                Timber.tag(TAG).d("cached is empty")
                emit(DataState.Loading())
            }

            var forecastDTO:ForecastDTO? = null

            val response = weatherServices.getWeeklyForecast(gridId, gridX, gridY)
            if(response.code() == 500){
                Log.d(TAG, "getWeather: Error code 500 alert!")
                throw ServerSideError("ServerSide Error with code 500")
            }
            try {
                Log.d(TAG, "getWeeklyForecast: code ${response.code()}")
                if(response.isSuccessful){
                    forecastDTO = response.body()

                    // * RoomDataBase.withTrasaction() is used to prevent any alternation to database
                    // * If any of transaction is failed, it will all cancel and data will be not changed.
                    weatherDataBase.withTransaction {
                        //store into Database if it is not empty
                        forecastDTO?.let{
                            weatherDataBase.forecastDao().insertForecast(it.toForecastEntity())
                        }

                        val forecast = weatherDataBase.forecastDao().getAllForecast()
                        forecast?.let{
                            //data = response.body()
                            Log.d(TAG, "getWeeklyForecast: success with DTO with updateTime of ${forecastDTO!!.propertiesDto.updateTime}")
                            emit(DataState.Success(it.toForecast()))
                        }
                    }
                }
            }catch (e:Exception) {
                Log.d(TAG, "getWeeklyForecast: Something went wrong")
                emit(returnErrorByType(response.code(), e))
            }
//s            Log.d(TAG, "getWeeklyForecast: ${forecast_dao.getAllForecast().properties.elevation}")
            //val forecast = forecast_dao.getAllForecast().toForecast()
            //emit(DataState.Success(data?.toForecastEntity()?.toForecast()))
        }
            .retry(2) { exception ->
                Log.d(TAG, "getWeeklyForecast: serverside error and retrying from repository")
                (exception is ServerSideError).also{if(it) delay(1000)}
            }
            .catch { exception ->
                Log.d(TAG, "getWeeklyForecast: inside catch block ${exception.cause}")
                emit(DataState.Error(exception.message ?: "Unknown Error from repository-getWeeklyForecast()"))
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