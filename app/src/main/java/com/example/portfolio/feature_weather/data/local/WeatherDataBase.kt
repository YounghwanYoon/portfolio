package com.example.portfolio.feature_weather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.portfolio.feature_weather.data.local.entity.WeatherEntity
import com.example.portfolio.feature_weather.data.utils.WeatherTypeConverter

@Database(entities = [WeatherEntity::class], version = 1)
@TypeConverters(WeatherTypeConverter::class)
abstract class WeatherDataBase:RoomDatabase() {
    companion object{
        const val DATABASE_NAME = "weather_db"
    }

    abstract fun weatherDao(): WeatherDao
}