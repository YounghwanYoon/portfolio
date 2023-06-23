package com.example.portfolio.feature_weather.di

import android.content.Context
import androidx.room.Room
import com.example.portfolio.feature_weather.data.local.WeatherDao
import com.example.portfolio.feature_weather.data.local.WeatherDataBase
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDao
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDataBase
import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.ForecastHourlyDao
import com.example.portfolio.feature_weather.data.utils.ForecastHourlyTypeConverter
import com.example.portfolio.feature_weather.data.utils.ForecastTypeConverter
import com.example.portfolio.feature_weather.data.utils.GsonParser
import com.example.portfolio.feature_weather.data.utils.WeatherTypeConverter
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherRoomModule {
    @Singleton
    @Provides
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDataBase {
        return Room.databaseBuilder(
            context,
            WeatherDataBase::class.java,
            WeatherDataBase.DATABASE_NAME
        )
            .addTypeConverter(WeatherTypeConverter(GsonParser(Gson())))
            .addTypeConverter(ForecastTypeConverter(GsonParser(Gson())))
            .addTypeConverter(ForecastHourlyTypeConverter(GsonParser(Gson())))
            .fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideWeatherDao(weatherDB: WeatherDataBase): WeatherDao {
        return weatherDB.weatherDao()
    }

    @Singleton
    @Provides
    fun provideForecastDao(weatherDB: WeatherDataBase): ForecastDao {
        return weatherDB.forecastDao()
    }

    @Singleton
    @Provides
    fun provideForecastHourlyDao(weatherDB: WeatherDataBase): ForecastHourlyDao {
        return weatherDB.forecastHourlyDao()
    }


}