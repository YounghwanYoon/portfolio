package com.example.portfolio.feature_weather.data.local.entity.forecast.entity

import androidx.room.PrimaryKey
import com.example.portfolio.feature_weather.domain.model.forecast.Properties

data class PropertiesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val elevation: ElevationEntity,
//    val forecastGenerator: String? = null,
//    val generatedAt: String? = null,
//    //This contains list of temperature data.
    val periods: List<PeriodEntity>? = null,
//    val units: String? = null,
    val updateTime: String? = null,
//    val updated: String? = null,
//    val validTimes: String? = null
){
    fun toProperties() = Properties(
        elevation = this.elevation.toElevation(),
        periods = this.periods?.map{it.toPeriod()},
        updateTime = this.updateTime
    )

}