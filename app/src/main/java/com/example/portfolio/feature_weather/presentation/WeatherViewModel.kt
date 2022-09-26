package com.example.portfolio.feature_myapp.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.utils.DataState
import com.example.portfolio.feature_weather.data.repository.WeatherRepositoryImpl
import com.example.portfolio.feature_weather.domain.use_case.GetWeatherInfo
import com.example.portfolio.feature_myapp.presentation.viewmodel.PermissionState.*
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepositoryImpl: WeatherRepositoryImpl,
    private val getWeatherInfo: GetWeatherInfo,
    private val dispatcherIO: CoroutineDispatcher,
    application: Application
): AndroidViewModel(application) {

    companion object{
        private const val TAG = "Weather_VM"
    }

/*    private val _dataState: MutableStateFlow<DataState<Forecast>> = MutableStateFlow(DataState.Loading)
    val dataState: StateFlow<DataState<Forecast>> = _dataState.asStateFlow()
    */
    private val _weatherState: MutableStateFlow<DataState<Weather>> = MutableStateFlow(DataState.Loading())
    val weatherState: StateFlow<DataState<Weather>> = _weatherState

    private val _forecastState:MutableStateFlow<DataState<Forecast>> = MutableStateFlow<DataState<Forecast>>(DataState.Loading())
    val forecastState:StateFlow<DataState<Forecast>> = _forecastState

    private val _forecastHourlyState:MutableStateFlow<DataState<ForecastHourly>> = MutableStateFlow<DataState<ForecastHourly>>(DataState.Loading())
    val forecastHourlyState:StateFlow<DataState<ForecastHourly>> = _forecastHourlyState

    private val _gpsState: MutableStateFlow<PermissionState> = MutableStateFlow(Requesting)
    val gpsState:StateFlow<PermissionState> = _gpsState.asStateFlow()

    /*fun getForecasts(currentGPS:Map<String,Int?>){
        Log.d(TAG, "getForecasts: is called with currentGPS= ${currentGPS["latitude"]}, ${currentGPS["longitude"]}")
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                getForecastInfo(currentGPS)
                    .collect { dataState ->
                        when(dataState){
                            is DataState.Error -> Log.d(TAG, "getForecasts: Error with Forecast () due to ${dataState.exception.message}")
                            DataState.Loading -> Log.d(TAG, "getForecasts: Loading")
                            is DataState.Success -> Log.d(TAG, "getForecasts: Success")
                            is DataState.SuccessSingleData -> {
                                Log.d(TAG, "getForecasts: Success & updated _dataState")
                                _dataState.value= dataState
                            }
                        }
                    }
            }
        }
    }*/

    private fun getWeather(currentGPS: CurrentGPS = CurrentGPS(gps= mutableMapOf("latitude" to 37, "longitude" to -122))){
        viewModelScope.launch {
            withContext(dispatcherIO){
                getWeatherInfo(currentGPS.gps!!).collect{ it ->
                    when(it){
                        is DataState.Error -> {
                            _weatherState.value = it
                            Log.d(TAG, "getWeather: Error with ${it.message}")
                        }
                        is DataState.Loading -> {
                            _weatherState.value = it
                            Log.d(TAG, "getWeather: Loading")
                        }
                        is DataState.Success -> {
                            Log.d(TAG, "getWeather: Success with ${it.data!!.properties.forecastOffice}")
                            //store weatherState
                            _weatherState.value = it
                            _weatherState.value.data!!.properties.apply{
                                launch{
                                    getForecast(gridId,gridX, gridY)
                                }
                                launch{
                                    getForecastHourly(gridId,gridX, gridY)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private suspend fun getForecast(gridId:String, gridX:Int, gridY:Int){
        getWeatherInfo.getForecast(gridId,gridX, gridY).collectLatest{ dataState ->
            when(dataState){
                is DataState.Error -> {
                    Log.d(TAG, "getForecast: Error() with ${dataState.message}")
                }
                is DataState.Loading -> {
                    Log.d(TAG, "getForecast: Loading()")
                }
                is DataState.Success -> {
                    Log.d(TAG, "getForecast: SUCCESS")
                }
            }
        }
    }

    private suspend fun getForecastHourly(gridId:String,gridX:Int,gridY:Int){
        getWeatherInfo.getForecastHourly(gridId,gridX, gridY).collectLatest{ dataState ->
            when(dataState){
                is DataState.Error -> {
                    Log.d(TAG, "getForecastHourly: Error() with ${dataState.message}")
                }
                is DataState.Loading -> {
                    Log.d(TAG, "getForecastHourly: Loading()")
                }
                is DataState.Success -> {
                    Log.d(TAG, "getForecastHourly: SUCCESS")
                }
            }
        }
    }
    private val _permState:MutableStateFlow<PermissionState> = MutableStateFlow<PermissionState>(
        PermissionState.Requesting
    )
    val permState:StateFlow<PermissionState> = _permState.asStateFlow()

    //update PermState
    fun setPermState(newPermState:PermissionState){
/*
        _permState.update{
            it.copy(
                isGPSGranted = newPermState.isGPSGranted,
                currentGPS = newPermState.currentGPS,
                currentTime = newPermState.currentTime
            )
        }
*/

        when(newPermState){
            is Requesting -> {
                _permState.value = newPermState
            }
            is Granted -> {
                _permState.value = newPermState

                getWeather(newPermState.gpsData)
            }

            is Error -> {
                _permState.value = newPermState
            }

        }

    }

}



data class GPSState(
    val isGPSGranted:Boolean = false,
    val currentGPS: CurrentGPS? = null,
    val currentTime:Long = 0,
)
data class CurrentGPS(
    var gps: Map<String, Int?>? = null
)

sealed class PermissionState(){
    data class Granted(val gpsData: CurrentGPS):PermissionState()
    class Error(val message:String):PermissionState()
    object Requesting:PermissionState()
    //data class Error(val gpsState: GPSState, val e:Exception):PermissionState()
}



