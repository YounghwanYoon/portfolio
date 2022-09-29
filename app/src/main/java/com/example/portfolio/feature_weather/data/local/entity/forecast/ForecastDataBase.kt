package com.example.portfolio.feature_weather.data.local.entity.forecast

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.portfolio.feature_weather.data.local.entity.forecast.entity.ForecastEntity
import com.example.portfolio.feature_weather.data.utils.ForecastTypeConverter

@Database(entities = [ForecastEntity::class], version = 1)
@TypeConverters(ForecastTypeConverter::class)
abstract class ForecastDataBase:RoomDatabase() {
    companion object{
        const val DATABASE_NAME = "forecast_db"
    }

    abstract fun forecastDao(): ForecastDao
}