package com.example.portfolio.feature_weather.data.local.entity.forecast

import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.portfolio.feature_weather.domain.model.forecast.Properties

data class PropertiesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val elevation: ElevationEntity,
//    val forecastGenerator: String,
//    val generatedAt: String,
//    //This contains list of temperature data.
    val periods: List<PeriodEntity>,
//    val units: String,
//    val updateTime: String,
//    val updated: String,
//    val validTimes: String
){
    fun toProperties() = Properties(
        elevation = this.elevation.toElevation(),
        periods = this.periods.map{it.toPeriod()}
    )

}