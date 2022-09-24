package com.example.portfolio.feature_weather.data.local.entity.forecasthourly

import androidx.room.*
import com.example.portfolio.feature_weather.data.local.WeatherDataBase

@Dao
interface ForecastHourlyDao {

    @Query("SELECT * FROM ${WeatherDataBase.DATABASE_NAME}_forecast_hourly")
    suspend fun getAllForecastHourly():ForecastHourlyEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastHourly(data: ForecastHourlyEntity): Long

    @Delete
    fun deleteForecastHourly(data: ForecastHourlyEntity)

}