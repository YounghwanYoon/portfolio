package com.example.portfolio.feature_weather.data.local

import androidx.room.*
import com.example.portfolio.feature_weather.data.local.entity.weather.WeatherEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM ${WeatherDataBase.DATABASE_NAME}")
    suspend fun getAll(): WeatherEntity

    // Long return at which row, the data was inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: WeatherEntity): Long

    @Delete
    fun deleteWeather(data: WeatherEntity)

}