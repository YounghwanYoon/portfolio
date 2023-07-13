package com.example.portfolio.feature_weather.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.utils.DataState
import com.example.portfolio.feature_weather.data.repository.WeatherRepositoryImpl
import com.example.portfolio.feature_weather.domain.use_case.GetWeatherInfo
import com.example.portfolio.feature_weather.presentation.PermissionState.*
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.model.forecast.Period
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import com.example.portfolio.utils.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepositoryImpl: WeatherRepositoryImpl,
    private val getWeatherInfo: GetWeatherInfo,
    private val dispatcherIO: CoroutineDispatcher,
    private val application: Application,
): AndroidViewModel(application) {

    companion object{
        private const val TAG = "Weather_VM"
    }
    private val _permState:MutableStateFlow<PermissionState> = MutableStateFlow<PermissionState>(
        PermissionState.Requesting
    )
    val permState:StateFlow<PermissionState> = _permState.asStateFlow()
    fun setPermState(newPermState: PermissionState){
        when(newPermState){
            is Requesting -> {
                Log.d(TAG, "setPermState: Requesting")
                _permState.update{
                    newPermState
                }
            }
            is Granted -> {
                Log.d(TAG, "setPermState: Granted calling getWeather()")
                //_permState.value = newPermState
                _permState.update {
                    newPermState
                }

                //getWeather(newPermState.currentGPS)
            }
            is Error -> {
                Log.d(TAG, "setPermState: Error with ${newPermState.message}")
                _permState.value = newPermState
            }

            Denied -> {
                Log.d(TAG, "setPermState: Denied")
                _permState.value = newPermState
            }
        }
    }

    private val _gpsState: MutableStateFlow<GPSState> = MutableStateFlow(GPSState.Requesting)
    val gpsState:StateFlow<GPSState> = _gpsState.asStateFlow()
    fun setGPSState(newState: GPSState){
        when(newState){
            is GPSState.Requesting -> {
                Log.d(TAG, "setPermState: Requesting")
                _gpsState.update{
                    newState
                }
            }
            is GPSState.Granted -> {
                Log.d(TAG, "setPermState: Granted calling getWeather()")
                //_gpsState.value = newState
                _gpsState.update {
                    newState
                }
                getWeather(newState.currentGPS)
            }

            is GPSState.Error -> {
                Log.d(TAG, "setPermState: Error with ${newState.message}")
                _gpsState.value = newState
            }

            GPSState.Denied -> {
                Log.d(TAG, "setPermState: Denied")
                _gpsState.value = newState
            }
        }
    }

    private val _weatherState: MutableStateFlow<DataState<Weather>> = MutableStateFlow(DataState.Loading())
    val weatherState: StateFlow<DataState<Weather>> = _weatherState
    private var forecastJob: Job? = null
    private var forecasthourlyJob: Job? = null

    private val _forecastListState = MutableSharedFlow<List<Period>>(
        replay = 1,
        onBufferOverflow =BufferOverflow.DROP_OLDEST
    )
    val forecastListState = _forecastListState.distinctUntilChanged()

    private val _forecastHourlyState = MutableSharedFlow<ForecastHourly?>(
        replay = 1
    )
    val forecastHourlyState:Flow<ForecastHourly?> = _forecastHourlyState.distinctUntilChanged()


    private fun getWeather(currentGPS: CurrentGPS = CurrentGPS(gps= mutableMapOf("latitude" to 37, "longitude" to -122))){
        viewModelScope.launch {
            getWeatherInfo(currentGPS.gps!!) .collect{ dataState ->
                when(dataState){
                        is DataState.Error -> {
                            _weatherState.value = dataState
                            Log.d(TAG, "getWeather: Error with ${dataState.message}")

                        }
                        is DataState.Loading -> {
                            _weatherState.value = dataState
                            Log.d(TAG, "getWeather: Loading")
                        }
                        is DataState.Success -> {
                            //updateWeatherRetry(Retry.RESET, )
                            Log.d(TAG, "getWeather: Success with ${dataState.data!!.properties.forecastOffice}")
                            //_weatherState.value = dataState
                            _weatherState.update {
                                dataState
                            }

                            //request forecast and forecasthoulry data from server.
                            _weatherState.value.data!!.properties.apply{
                                val forecast = async{
                                    getForecast(gridId,gridX, gridY) }
                                val forecastHourly = async{
                                    getForecastHourly(gridId,gridX, gridY)
                                }
                                awaitAll(forecast,forecastHourly)
                            }
                        }
            }
            }
        }
    }
    private suspend fun getForecast(gridId:String, gridX:Int, gridY:Int){
        forecastJob?.cancel()
        forecastJob = viewModelScope.launch{
            //delay(1000)
            getWeatherInfo.getForecast(gridId,gridX, gridY)
                .distinctUntilChanged()
                .onEach{ dataState ->
                    when(dataState){
                        is DataState.Error -> {
                            Log.d(TAG, "getWeeklyForecast: Error() with ${dataState.message}")
                        }
                        is DataState.Loading -> {
                            Log.d(TAG, "getWeeklyForecast: Loading()")
                        }
                        is DataState.Success -> {

                            _forecastListState.emit(dataState.data!!.properties.periods!!)


                            //_forecastListState.value = dataState.data?.properties?.periods ?: emptyList()

                            //_forecastState.value = dataState.data?.copy()

                            forecastListState.onEach {
                                Log.d(TAG, "getWeeklyForecast: ${it.size}")

                            }

                        }
                    }
            }
                .launchIn(this)
        }
    }
    private suspend fun getForecastHourly(gridId:String,gridX:Int,gridY:Int){
        forecasthourlyJob?.cancel()
        forecasthourlyJob = viewModelScope.launch{
            //delay(1000)
            getWeatherInfo.getForecastHourly(gridId,gridX, gridY)
                .distinctUntilChanged()
                .onEach{ dataState ->
                    when(dataState){
                        is DataState.Error -> {
                            Log.d(TAG, "getForecastHourly: Error() with ${dataState.message}")
                        }
                        is DataState.Loading -> {
                            Log.d(TAG, "getForecastHourly: Loading()")
                        }

                        /*is DataState.Success -> {
                            Log.d(TAG, "getForecastHourly: Success from VM with ${_forecastHourlyState.value?.properties?.updateTime}")


                            withContext(Dispatchers.Main){
                                _forecastHourlyState.value = dataState.data
                            }

                            //withContext(Dispatchers.Main){
                             *//*   _forecastHourlyState.update{
                                    it?.copy(
                                        properties = dataState.data!!.properties,
                                        type = dataState.data.type
                                    ) ?: dataState.data
                                }*//*
                                Log.d(TAG, "getForecastHourly: Success from VM with ${forecastHourlyState.value?.properties?.updateTime}")
                            //}
                        }*/
                        is DataState.Success -> {
                            _forecastHourlyState.emit(dataState.data)
                        }
                    }
            }
                .launchIn(this)
        }
    }

}


data class CurrentGPS(
    var gps: Map<String, Int?>? = null,
    var isInternetAvaiable:Boolean = false,
    var isGPSEnabled:Boolean = false,
)
sealed class PermissionState(){
    //data class Granted(val currentGPS: CurrentGPS): PermissionState()
    object Granted: PermissionState()
    object Denied: PermissionState()
    class Error(val message:String): PermissionState()
    object Requesting: PermissionState()
    //data class Error(val gpsState: GPSState, val e:Exception):PermissionState()
}

sealed class GPSState(){
    data class Granted(val currentGPS: CurrentGPS): GPSState()
    object Denied: GPSState()
    class Error(val message:String): GPSState()
    object Requesting: GPSState()
    //data class Error(val gpsState: GPSState, val e:Exception):PermissionState()
}



