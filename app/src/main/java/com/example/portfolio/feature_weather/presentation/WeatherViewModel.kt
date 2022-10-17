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
import com.example.portfolio.feature_weather.domain.model.forecast.Period
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

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

    private val _weatherState: MutableStateFlow<DataState<Weather>> = MutableStateFlow(DataState.Loading())
    val weatherState: StateFlow<DataState<Weather>> = _weatherState

/*    private val _forecastState:MutableStateFlow<Forecast?> = MutableStateFlow<Forecast?>(null)
    val forecastState:StateFlow<Forecast?> = _forecastState*/
   /* val _forecastListState:MutableStateFlow<List<Period>?> = MutableStateFlow<List<Period>?>(emptyList())
    val forecastListState:StateFlow<List<Period>?> = _forecastListState
*/
    private val _forecastListState = MutableSharedFlow<List<Period>>(
        replay = 1,
        onBufferOverflow =BufferOverflow.DROP_OLDEST
    )
    val forecastListState = _forecastListState.distinctUntilChanged()


/*
    private val _forecastState:MutableStateFlow<DataState<Forecast>> = MutableStateFlow<DataState<Forecast>>(DataState.Loading())
    val forecastState = _forecastState as StateFlow<DataState<Forecast>>
*/
/*    private val _forecastHourlyState:MutableStateFlow<ForecastHourly?> = MutableStateFlow<ForecastHourly?>(null)
    val forecastHourlyState:StateFlow<ForecastHourly?> = _forecastHourlyState  */

    private val _forecastHourlyState = MutableSharedFlow<ForecastHourly?>(
        replay = 1
    )
    val forecastHourlyState:Flow<ForecastHourly?> = _forecastHourlyState.distinctUntilChanged()

    private val _gpsState: MutableStateFlow<PermissionState> = MutableStateFlow(Requesting)
    val gpsState:StateFlow<PermissionState> = _gpsState.asStateFlow()

    private fun getWeather(currentGPS: CurrentGPS = CurrentGPS(gps= mutableMapOf("latitude" to 37, "longitude" to -122))){
        viewModelScope.launch {
            //withContext(dispatcherIO){
                getWeatherInfo(currentGPS.gps!!).collect{ dataState ->
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
                            Log.d(TAG, "getWeather: Success with ${dataState.data!!.properties.forecastOffice}")
                            //_weatherState.value = dataState
                            _weatherState.update {
                                dataState
                            }


                            //request forecast and forecasthoulry data from server.
                            _weatherState.value.data!!.properties.apply{

                                //viewModelScope.launch {

                                    val forecast = async{
                                        getForecast(gridId,gridX, gridY) }
                                    val forecastHourly = async{
                                        getForecastHourly(gridId,gridX, gridY)
                                    }

                                    awaitAll(forecast,forecastHourly)

                                    

                                //}


                            }

                        }
                    }
                }
            //}
        }
    }

    private var forecastJob: Job? = null

    private suspend fun getForecast(gridId:String, gridX:Int, gridY:Int){
        forecastJob?.cancel()
        forecastJob = viewModelScope.launch{
            delay(1000)
            getWeatherInfo.getForecast(gridId,gridX, gridY)
                .distinctUntilChanged()
                .onEach{ dataState ->
                    when(dataState){
                        is DataState.Error -> {
                            Log.d(TAG, "getForecast: Error() with ${dataState.message}")
                        }
                        is DataState.Loading -> {
                            Log.d(TAG, "getForecast: Loading()")
                        }
                        is DataState.Success -> {

                            _forecastListState.emit(dataState.data!!.properties.periods!!)


                            //_forecastListState.value = dataState.data?.properties?.periods ?: emptyList()

                            //_forecastState.value = dataState.data?.copy()

                            forecastListState.onEach {
                                Log.d(TAG, "getForecast: ${it.size}")

                            }

                        }
                    }
            }
                .launchIn(this)
        }
    }


    private var forecasthourlyJob: Job? = null
    private suspend fun getForecastHourly(gridId:String,gridX:Int,gridY:Int){
        forecasthourlyJob?.cancel()
        forecasthourlyJob = viewModelScope.launch{
            delay(1000)
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
                //_permState.value = newPermState
                _permState.update {
                    newPermState
                }

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



