package com.example.portfolio.feature_weather.data.repository

import android.util.Log
import com.example.portfolio.utils.DataState
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.data.local.WeatherDao
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDao
import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.ForecastHourlyDao
import com.example.portfolio.feature_weather.data.remote.dto.WeatherDto
import com.example.portfolio.feature_weather.data.remote.dto.forecast_dto.ForecastDto
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import com.example.portfolio.feature_weather.domain.repository.WeatherRepository
import com.example.portfolio.feature_weather.domain.repository.WeatherServices
import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineScope.*
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.launch

class WeatherRepositoryImpl @Inject constructor(
    private val weatherServices: WeatherServices,
    private val weather_dao: WeatherDao? = null,
    private val forecast_dao: ForecastDao,
    private val forecastHourly_dao: ForecastHourlyDao,
    private val ioDispatcher:CoroutineDispatcher = Dispatchers.IO
    ): WeatherRepository{

  /*  override suspend fun getWeather(gpsData:Map<String, Int?>):Flow<DataState<WeatherDto>>{
        Log.d(TAG, "getWeather: getWeather is called from repository implement with gps${gpsData["latitude"]!!} ${gpsData["longitude"]}")
        return flow<DataState<WeatherDto>> {
            emit(DataState.Loading)
            try{
                val response = weatherServices.getWeather(gpsData["latitude"]!!, gpsData["longitude"]!!)
                Log.d(TAG, "getWeather: response code is ${response.code()}")
                if(response.isSuccessful && response.body() != null){
                    response.body()?.let{
                        Log.d(TAG, "getWeatherData:weather id is ${ it.id}")
                        emit(DataState.SuccessSingleData(it))
                    }
                }else{
                    Log.d(TAG, "getWeather: response from server was not successful ${response.errorBody().toString()}")
                    emit(DataState.Error(Exception(response.errorBody().toString() + "error code: ${response.code()}" ) ))
                }
            }
            catch (e:HttpException){
                //type mismatch exception
                emit(DataState.Error(Exception("HttpException occurred")))
            }
            catch(e: IOException){
                //input & output failure or data/directory failed such as server is not found
                emit(DataState.Error(Exception("IOException occurred")))
            }
        }.flowOn(ioDispatcher).catch {
            emit(DataState.Error(Exception("getWeather: Error occurred from getWeather from repository implement")))
        }

    }
    override suspend fun getForecast(gpsData:Map<String, Int?>): Flow<DataState<Forecast>> {
        Log.d(TAG, "getForecast: getForecast is called from repository implement")

        return flow{
            emit(DataState.Loading)
            try{
                getWeather(gpsData).collect{ state ->
                    when(state){
                        is DataState.Error ->{
                            Log.d(TAG, "getForecast: failed to received weather data due to ${state.exception.message}")
                            emit(DataState.Error(Exception("went wrong from getWeather - ${state.exception.message}")))
                        }
                        DataState.Loading -> Log.d(TAG, "getForecast: loading weather data")
                        is DataState.Success -> Log.d(TAG, "getForecast: Success will not be used in this app")
                        is DataState.SuccessSingleData -> {
                            Log.d(TAG, "getForecast: SucessSingleData ")
                            try {
                                val office = state.data?.properties?.gridId
                                val gridX = state.data?.properties?.gridX
                                val gridY = state.data?.properties?.gridY
                                //val response: Response<ForecastDto>
                                val response =
                                    weatherServices.getForecast(office!!, gridX!!, gridY!!)
                                Log.d(TAG, "getForecast: response code = ${response.code()}")
                                when (response.isSuccessful) {
                                    true -> {
                                        if (response.body() != null) {
                                            // Map/transform to Dto to Entity
                                            // and insert it to Room
                                            // get Data from Room
                                            // Convert it to model
                                            // emit data
                                            val newData = response.body()!!.toForecastEntity()
                                            forecast_dao.delete(newData)
                                            forecast_dao.insert(newData)

                                            val localData = forecast_dao.getAll().toForecast()
                                            emit(DataState.SuccessSingleData(localData))
                                        } else {
                                            emit(DataState.Error(Exception("Requesting 'Forest' response is successful without body.")))
                                        }
                                    }
                                    false -> {
                                        emit(
                                            DataState.Error(
                                                Exception(
                                                    "Something went wrong for requesting Forest " +
                                                            "${response.errorBody()} with error code: ${response.code()}"
                                                )
                                            )
                                        )
                                    }
                                }
                            }catch(e:Exception){
                                emit(DataState.Error(e))
                            }
                            emit(DataState.SuccessSingleData(forecast_dao.getAll().toForecast()))
                        }
                    }
                }
            }
            catch (e:HttpException){
                //type mismatch exception
                emit(DataState.Error(Exception("HttpException occurred")))
            }
            catch(e: IOException){
                //input & output failure or data/directory failed such as server is not found
                emit(DataState.Error(Exception("IOException occurred")))
            }

        }.flowOn(ioDispatcher)
    }
*/

    override fun getWeather(gpsData:Map<String, Int?>):Flow<DataState<Weather>>{
        Log.d(TAG, "getWeather: getWeather is called from repository implement with gps${gpsData["latitude"]!!} ${gpsData["longitude"]}")
        return flow<DataState<Weather>>{
            this.emit(DataState.Loading())
            try{
                val response = weatherServices.getWeather(gpsData["latitude"]!!, gpsData["longitude"]!!)
                Log.d(TAG, "getWeather: response code ${response.code()}")
                if(response.isSuccessful){
                    val body = response.body()
                    val weather = body!!.toWeather()
                    emit(DataState.Success(weather))
                }
            }catch (e:Exception){
                Log.d(TAG, "getWeather: something went off")
                emit(DataState.Error(e.message!!))
            }
        }.flowOn(ioDispatcher)
    }
    override fun getForecast(gridId:String,gridX:Int,gridY:Int): Flow<DataState<Forecast>> {
        Log.d(TAG, "getForecast: getForecast is called from repository implement")


        return flow<DataState<Forecast>>{
            emit(DataState.Loading())
/*            val entities = forecast_dao!!.getAllForecast()
            entities?.let{
                emit(DataState.Loading(entities.toForecast()))
            }*/
            var data:ForecastDto? = null

            try {
                val response = weatherServices.getForecast(gridId, gridX, gridY)
                Log.d(TAG, "getForecast: code ${response.code()}")
                if(response.isSuccessful){
                    data = response.body()
                    //data = response.body()
                    Log.d(TAG, "getForecast: success with DTO with updateTime of ${data?.propertiesDto?.updateTime}")
                }
                //store to local data ...causing so much error
/*
                val forecastEntity = data?.toForecastEntity()

                forecastEntity?.let{
                    Log.d(TAG, "getForecast: converting to DTO -> Entity ${it.properties.elevation}")
                    //Insert data from server to local db
                    forecast_dao.insertForecast(forecastEntity)
                }*/

            }catch (e:Exception) {
                when(e){
                    is HttpException ->{
                        emit(DataState.Error<Forecast>("Internet Error with : ${ e.message}"))
                    }
                    else ->{
                        emit(DataState.Error<Forecast>("Error from using Dagger: ${ e.message}"))
                    }
                }
            }
//s            Log.d(TAG, "getForecast: ${forecast_dao.getAllForecast().properties.elevation}")
            //val forecast = forecast_dao.getAllForecast().toForecast()
            emit(DataState.Success(data?.toForecastEntity()?.toForecast()))
        }//.flowOn(ioDispatcher)
    }
    override fun getForecastHourly(gridId:String,gridX:Int,gridY:Int): Flow<DataState<ForecastHourly>> {
        Log.d(TAG, "getForecastHourly: getForecastHourly is called from repository implement")
        return flow{
            emit(DataState.Loading())
/*            val entities = forecastHourly_dao!!.getAllForecastHourly()
            if(entities != null)
            entities?.let{
                emit(DataState.Loading(entities.toForecastHourly()))
            }*/
            try{
                val response = weatherServices.getForecastHourly(gridId,gridX,gridY)
                val data = response.body()
                if(data == null)
                    emit(DataState.Error("Error from getForecastHourly inside RepoImpl and Code = ${response.code()} "))

                //Insert data from server to local db
                data?.let{
                    forecastHourly_dao!!.insertForecastHourly(data.toForecastHourlyEntity())
                }
                //get data from local db
                val forecastHourly = forecastHourly_dao!!.getAllForecastHourly().toForecastHourly()

                //emit data for observer/client
                emit(DataState.Success(forecastHourly))
            }catch(e:Exception){
                val message = "From getForecastHourly() inside RepoImp Error occurred with : ${e.message} "
                emit(DataState.Error(message))
            }
        }.flowOn(ioDispatcher)
    }

    companion object{
        private val TAG:String = this.javaClass.name
    }

}