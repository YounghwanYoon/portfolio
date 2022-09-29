package com.example.portfolio.feature_weather.data.remote.dto.forecast_dto

import com.example.portfolio.feature_weather.data.local.entity.forecast.entity.ForecastEntity
import com.google.gson.annotations.SerializedName


data class ForecastDto(
    //val geometry: Geometry,
    @SerializedName("properties")
    val propertiesDto: PropertiesDto,
    //val type: String
){
    fun toForecastEntity(): ForecastEntity {
        return ForecastEntity(
            properties = this.propertiesDto.toProperties()
        )
    }
}