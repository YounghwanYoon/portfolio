package com.example.portfolio.feature_weather.data.local.entity.forecasthourly.entity

import androidx.room.PrimaryKey
import com.example.portfolio.feature_weather.domain.model.forecasthourly.Properties


data class PropertiesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val elevation: ElevationEntity,
    val forecastGenerator: String,
    val generatedAt: String,
    val periods: List<PeriodEntity>,
    val units: String,
    val updateTime: String,
    val updated: String,
    val validTimes: String
){
    fun toProperties() = Properties(
         elevation = elevation.toElevation(),
         forecastGenerator = forecastGenerator,
         generatedAt  =generatedAt,
         periods = periods.map{it.toPeriod()},
         units = units,
         updateTime =updateTime,
         updated = updated,
        validTimes = validTimes
    )
}