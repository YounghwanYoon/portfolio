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
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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

    private val _forecastState:MutableStateFlow<Forecast?> = MutableStateFlow<Forecast?>(null)
    val forecastState:StateFlow<Forecast?> = _forecastState

    private val _forecastHourlyState:MutableStateFlow<ForecastHourly?> = MutableStateFlow<ForecastHourly?>(null)
    val forecastHourlyState:StateFlow<ForecastHourly?> = _forecastHourlyState

    private val _gpsState: MutableStateFlow<PermissionState> = MutableStateFlow(Requesting)
    val gpsState:StateFlow<PermissionState> = _gpsState.asStateFlow()

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
                            _weatherState.value = it

                            //request forecast and forecasthoulry data from server.
                            _weatherState.value.data!!.properties.apply{

                                val forecast = async{
                                    getForecast(gridId,gridX, gridY) }
                                val forecastHourly = async{
                                    getForecastHourly(gridId,gridX, gridY)
                                }

                                awaitAll(forecast,forecastHourly)
                            }

                            //store weatherState

                            /*  _weatherState.value.data!!.properties.apply{
                                launch{
                                    getForecast(gridId,gridX, gridY)
                                }
                                launch{
                                    getForecastHourly(gridId,gridX, gridY)
                                }
                            }*/

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
                    withContext(Dispatchers.Main){
                        Log.d(TAG, "getForecast: SUCCESS from VM ${dataState.data!!.properties.updateTime}")
                        _forecastState.update {
                            it?.copy(
                                properties = dataState.data.properties,
                            ) ?: dataState.data
                        }
                    }
                    Log.d(TAG, "getForecast: Success from VM with ${_forecastState.value?.properties?.updateTime}")
                }
            }
        }
    }
    private suspend fun getForecastHourly(gridId:String,gridX:Int,gridY:Int){
        getWeatherInfo.getForecastHourly(gridId,gridX, gridY).collect{ dataState ->
            when(dataState){
                is DataState.Error -> {
                    Log.d(TAG, "getForecastHourly: Error() with ${dataState.message}")
                }
                is DataState.Loading -> {
                    Log.d(TAG, "getForecastHourly: Loading()")
                }
                is DataState.Success -> {
                    Log.d(TAG, "getForecastHourly: SUCCESS ")
                    _forecastHourlyState.update{
                        it?.copy(
                            properties = dataState.data!!.properties,
                            type = dataState.data.type
                        )
                            ?: dataState.data
                    }

                }
            }
        }
    }




    private val _permState:MutableStateFlow<PermissionState> = MutableStateFlow<PermissionState>(
        PermissionState.Requesting
    )
    val permState:StateFlow<PermissionState> = _permState.asStateFlow()

    //update PermState
    fun setPermState(newPermState: PermissionState){
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

                getWeather(newPermState.currentGPS)
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
    data class Granted(val currentGPS: CurrentGPS): PermissionState()
    class Error(val message:String): PermissionState()
    object Requesting: PermissionState()
    //data class Error(val gpsState: GPSState, val e:Exception):PermissionState()
}



