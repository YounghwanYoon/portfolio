package com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto

import com.example.portfolio.feature_weather.data.local.entity.forecast.entity.ElevationEntity

data class ElevationDto(
    val unitCode: String,
    val value: Double
){
    fun toElevation(): ElevationEntity {
        return ElevationEntity(
            unitCode = this.unitCode,
            value = this.value
        )
    }

}