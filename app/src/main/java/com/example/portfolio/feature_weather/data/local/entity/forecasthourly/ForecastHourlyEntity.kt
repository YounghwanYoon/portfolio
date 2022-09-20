package com.example.portfolio.feature_weather.data.local.entity.forecasthourly

import com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto.GeometryEntity
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import com.google.gson.annotations.SerializedName

data class ForecastHourlyEntity(
    @SerializedName("@context")
    val context: List<Any>,
    val geometry: GeometryEntity,
    val properties: PropertiesEntity,
    val type: String
){
    fun toForecastHourly() = ForecastHourly(
        context= context,
        geometry = geometry.toGeometry(),
        properties = properties.toProperties(),
        type = type

    )
}