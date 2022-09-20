package com.example.portfolio.feature_weather.data.local

import androidx.room.*
import com.example.portfolio.feature_weather.data.local.entity.WeatherEntity
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastEntity
import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.ForecastHourlyEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_db")
    suspend fun getAll(): WeatherEntity
    @Query ("SELECT * FROM weather_db")
    suspend fun getAllForecast():ForecastEntity
    @Query ("SELECT * FROM weather_db")
    suspend fun getAllForecastHourly():ForecastHourlyEntity

    // Long return at which row, the data was inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: WeatherEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(data:ForecastEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastHourly(data: ForecastHourlyEntity): Long

    @Delete
    fun deleteWeather(data: WeatherEntity)
    @Delete
    fun deleteForecast(data: ForecastEntity)
    @Delete
    fun deleteForecastHourly(data: ForecastHourlyEntity)

}