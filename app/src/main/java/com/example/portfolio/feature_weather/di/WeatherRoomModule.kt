package com.example.portfolio.feature_weather.di

import android.content.Context
import androidx.room.Room
import com.example.portfolio.feature_weather.data.local.WeatherDao
import com.example.portfolio.feature_weather.data.local.WeatherDataBase
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDao
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDataBase
import com.example.portfolio.feature_weather.data.utils.ForecastTypeConverter
import com.example.portfolio.feature_weather.data.utils.GsonParser
import com.example.portfolio.feature_weather.data.utils.WeatherTypeConverter
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
            .fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideWeatherDB(weatherDB: WeatherDataBase): WeatherDao {
        return weatherDB.weatherDao()
    }

    @Singleton
    @Provides
    fun provideForecastDatabase(@ApplicationContext context: Context): ForecastDataBase {
        return Room.databaseBuilder(
            context,
            ForecastDataBase::class.java,
            ForecastDataBase.DATABASE_NAME
        )
            .addTypeConverter(ForecastTypeConverter(GsonParser(Gson())))
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideForecastDB(forecastDB: ForecastDataBase): ForecastDao {
        return forecastDB.forecastDao()
    }


}