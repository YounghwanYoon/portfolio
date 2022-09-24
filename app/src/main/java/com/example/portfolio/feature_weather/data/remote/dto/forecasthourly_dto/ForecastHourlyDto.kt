package com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto

import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.ForecastHourlyEntity
import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.PropertiesEntity
import com.google.gson.annotations.SerializedName

data class ForecastHourlyDto(
    @SerializedName("@context")
    val context: List<Any>,
    val geometry: GeometryDto,
    val properties: PropertiesEntity,
    val type: String
){
    fun toForecastHourlyEntity()= ForecastHourlyEntity(
        //context = context,
        //geometry = geometry.toGeometryEntity(),

        properties = properties,
        type = type
    )
}