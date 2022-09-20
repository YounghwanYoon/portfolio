package com.example.portfolio.feature_weather.data.local.entity.forecast

import androidx.room.*

@Dao
interface ForecastDao {

    @Query("SELECT * FROM ${ForecastDataBase.DATABASE_NAME}")
    suspend fun getAll(): ForecastEntity?

    // Long return at which row, the data was inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecastEntity: ForecastEntity): Long

    @Delete
    fun delete(forecastEntity: ForecastEntity)

}