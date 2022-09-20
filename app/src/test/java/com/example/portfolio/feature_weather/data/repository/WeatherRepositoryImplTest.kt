package com.example.portfolio.feature_weather.data.repository

import android.util.Log
import com.example.portfolio.feature_weather.data.BaseMockServerTest
import com.example.portfolio.feature_weather.data.local.WeatherDao
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDao
import com.example.portfolio.feature_weather.data.remote.dto.WeatherDto
import com.example.portfolio.feature_weather.di.WeatherModule
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.repository.WeatherRepository
import com.example.portfolio.feature_weather.domain.repository.WeatherServices
import com.example.portfolio.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryImplTest:BaseMockServerTest(){
    companion object {
        private const val TAG = "WeatherRepositoryImplTe"
    }

    private lateinit var repository: WeatherRepository
    private lateinit var dispatcher: TestDispatcher
    @Mock
    private lateinit var mockService: WeatherServices

    @Mock
    private lateinit var mockWeatherDao:WeatherDao

    @Mock
    private lateinit var mockForecastDao:ForecastDao
    val fakeGPS = mutableMapOf<String,Int>("latitude" to 32 , "longitude" to -122)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    override fun setUp()= runTest{
        MockitoAnnotations.openMocks(WeatherServices::class)

        val mockResponse = MockResponse().setResponseCode(200).setBody("{}")
        mockServer.enqueue(mockResponse)

        mockWeatherDao = mock(WeatherDao::class.java)
        mockForecastDao = mock(ForecastDao::class.java)
        mockService = mock(WeatherServices::class.java)
        dispatcher =  StandardTestDispatcher(testScheduler)

        repository = WeatherRepositoryImpl(mockService,mockWeatherDao, mockForecastDao,dispatcher)

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoGetWeatherRetrieveTest()= runTest{

        repository.getWeather(fakeGPS)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetWeather() = runTest{
        retrofit = getRetrofit("https://api.weather.gov/")
        val service = retrofit.create(WeatherServices::class.java)
        repository = WeatherRepositoryImpl(service,mockWeatherDao,mockForecastDao, dispatcher)
/*
        //weatherServices.getWeather(gpsData["latitude"]!!, gpsData["longitude"]!!)
        val response = service.getWeather(fakeGPS["latitude"]!!, fakeGPS["longitude"]!!)
        val result = response.isSuccessful

        assertEquals(true, result)*/

        var testResult:DataState<Weather> = DataState.Loading()
        repository.getWeather(fakeGPS).collect{
            when(it){
                is DataState.Error -> Log.d(TAG, "testGetWeather: Error")
                is DataState.Loading -> Log.d(TAG, "testGetWeather: Loading")
                is DataState.Success -> Log.d(TAG, "testGetWeather: SuccessSingleData")
            }
            testResult = it
        }
        assertEquals(DataState.Loading<Weather>(), testResult)




    }


}