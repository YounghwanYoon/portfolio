package com.example.portfolio.feature_weather.data.remote.dto.forecast_dto

import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastEntity


data class ForecastDto(
    //val geometry: Geometry,
    val propertiesDto: PropertiesDto,
    //val type: String
){
    fun toForecastEntity(): ForecastEntity {
        return ForecastEntity(
            properties = this.propertiesDto.toProperties()
        )
    }
}