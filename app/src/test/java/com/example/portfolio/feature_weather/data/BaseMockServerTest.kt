package com.example.portfolio.feature_weather.data

import com.example.portfolio.feature_weather.di.WeatherModule
import com.example.portfolio.feature_weather.domain.repository.WeatherServices
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.net.URL

open class BaseMockServerTest {
    var retrofit = WeatherModule.provideRetrofitBuilder()
    val mockServer = MockWebServer()

    private var modifier = 1
    inner class testingModifier(){
        fun testing(){
            modifier++
        }
    }

    fun getRetrofit(url:String? = null) = WeatherModule.provideRetrofitBuilder(url)

    @Before
    open fun setUp(){
        startServer()
    }

    fun startServer(url: String=""){
        mockServer.url(url)
        mockServer.start()
    }

    @After
    open fun tearDown(){
        mockServer.shutdown()
    }

}