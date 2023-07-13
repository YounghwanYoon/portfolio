package com.example.portfolio.feature_weather.di

import android.content.Context
import com.example.portfolio.feature_weather.data.repository.WeatherRepositoryImpl
import com.example.portfolio.feature_weather.data.local.WeatherDao
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDao
import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.ForecastHourlyDao
import com.example.portfolio.feature_weather.domain.repository.WeatherRepository
import com.example.portfolio.feature_weather.domain.repository.WeatherServices
import com.example.portfolio.feature_weather.domain.use_case.GetWeatherInfo
import com.example.portfolio.utils.LocationPermissionHandler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideGetInfoUseCase(
        repository: WeatherRepository
    ): GetWeatherInfo{
        return GetWeatherInfo(repository)
    }

    @Singleton
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }


/*    @Singleton
    @Provides
    fun provideWeatherRepository(
        weatherApiServices: WeatherServices,
        weather_dao: WeatherDao,
        forecast_dao: ForecastDao,
        forecastHourly_dao:ForecastHourlyDao,
        dispatcherIO:CoroutineDispatcher
    ): WeatherRepository {
        return WeatherRepositoryImpl(
            weatherApiServices,
            weather_dao,
            forecast_dao,
            forecastHourly_dao,
            dispatcherIO
        )
    }*/

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class WeatherRepositoryModule{
        @Binds
        abstract fun bindWeatherRepository(weatherRepository:WeatherRepositoryImpl):WeatherRepository

        /*companion object{


            *//*
             @Singleton
             @Provides
             fun provideGetInfoUseCase(
                 repository: WeatherRepository
             ): GetWeatherInfo{
                 return GetWeatherInfo(repository)
             }*//*


        }*/
    }
}

