package com.example.portfolio.feature_weather.di

import com.example.portfolio.feature_weather.domain.repository.WeatherServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@Named("WeatherModule")
@InstallIn(SingletonComponent::class)
object WeatherModule {

    private const val BASE_URL = "https://api.weather.gov/"

    @Singleton
    @Provides
    fun provideBaseURL() = BASE_URL

    @Singleton
    @Named("WeatherRetrofitInstance")
    @Provides
    fun provideRetrofitBuilder(baseURL:String? = BASE_URL): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Singleton
    @Provides
    fun provideApiServices(@Named("WeatherRetrofitInstance")retrofit:Retrofit): WeatherServices {
        return retrofit.create(WeatherServices::class.java)
    }
}