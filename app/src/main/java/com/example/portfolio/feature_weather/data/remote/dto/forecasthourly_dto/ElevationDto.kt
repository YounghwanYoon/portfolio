package com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto

import com.example.portfolio.feature_weather.data.local.entity.forecast.ElevationEntity
import com.example.portfolio.feature_weather.domain.model.forecast.Elevation

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