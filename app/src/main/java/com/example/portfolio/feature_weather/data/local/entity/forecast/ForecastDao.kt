package com.example.portfolio.feature_weather.data.local.entity.forecast

import androidx.room.*
import com.example.portfolio.feature_weather.data.local.WeatherDataBase
import com.example.portfolio.feature_weather.data.local.entity.forecast.entity.ForecastEntity

@Dao
interface ForecastDao {

    @Query ("SELECT * FROM ${WeatherDataBase.DATABASE_NAME}_forecast")
    suspend fun getAllForecast(): ForecastEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(data: ForecastEntity): Long

    // Long return at which row, the data was inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecastEntity: ForecastEntity): Long

    @Delete
    suspend fun delete(forecastEntity: ForecastEntity)

}