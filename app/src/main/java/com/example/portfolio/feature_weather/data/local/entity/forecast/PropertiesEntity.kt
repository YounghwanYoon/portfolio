package com.example.portfolio.feature_weather.data.local.entity.forecast

import com.example.portfolio.feature_weather.domain.model.forecast.Properties

data class PropertiesEntity(
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