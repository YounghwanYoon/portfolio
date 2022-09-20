package com.example.portfolio.feature_weather.di

import com.example.portfolio.feature_weather.data.repository.WeatherRepositoryImpl
import com.example.portfolio.feature_weather.data.local.WeatherDao
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDao
import com.example.portfolio.feature_weather.domain.repository.WeatherRepository
import com.example.portfolio.feature_weather.domain.repository.WeatherServices
import com.example.portfolio.feature_weather.domain.use_case.GetWeatherInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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


    @Singleton
    @Provides
    fun provideWeatherRepository(
        weatherApiServices: WeatherServices,
        weather_dao: WeatherDao,
        forecast_dao: ForecastDao,
        dispatcherIO:CoroutineDispatcher
    ): WeatherRepository {
        return WeatherRepositoryImpl(
            weatherApiServices,
            weather_dao,
            forecast_dao,
        )
    }


}