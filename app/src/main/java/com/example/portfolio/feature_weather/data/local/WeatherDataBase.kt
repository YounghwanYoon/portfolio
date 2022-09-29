package com.example.portfolio.feature_weather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.portfolio.feature_weather.data.local.entity.weather.WeatherEntity
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastDao
import com.example.portfolio.feature_weather.data.local.entity.forecast.entity.ForecastEntity
import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.ForecastHourlyDao
import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.entity.ForecastHourlyEntity
import com.example.portfolio.feature_weather.data.utils.ForecastHourlyTypeConverter
import com.example.portfolio.feature_weather.data.utils.ForecastTypeConverter
import com.example.portfolio.feature_weather.data.utils.WeatherTypeConverter

@Database(entities = [WeatherEntity::class, ForecastEntity::class, ForecastHourlyEntity::class], version = 1)
@TypeConverters(WeatherTypeConverter::class, ForecastTypeConverter::class, ForecastHourlyTypeConverter::class)
abstract class WeatherDataBase:RoomDatabase() {
    companion object{
        const val DATABASE_NAME = "weather_db"
    }

    abstract fun weatherDao(): WeatherDao
    abstract fun forecastDao():ForecastDao
    abstract fun forecastHourlyDao():ForecastHourlyDao
}