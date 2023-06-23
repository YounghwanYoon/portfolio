package com.example.portfolio.feature_weather.data.remote.dto.forecast_dto

import com.example.portfolio.feature_weather.data.local.entity.forecast.entity.PropertiesEntity
import com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto.ElevationDto

data class PropertiesDto(
    val elevation: ElevationDto,
    val forecastGenerator: String,
    val generatedAt: String,
    //This contains list of temperature data.
    val periods: List<PeriodDto>,
    val units: String,
    val updateTime: String,
    val updated: String,
    val validTimes: String
){
    fun toProperties(): PropertiesEntity {
        return PropertiesEntity(
            elevation = this.elevation.toElevation(),
            periods = this.periods.map{it.toPeriodEntity()}
        )
    }
}