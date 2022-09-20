package com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto

import com.example.portfolio.feature_weather.domain.model.forecasthourly.Geometry

data class GeometryEntity(
    val coordinates: List<List<List<Double>>>,
    val type: String
){
    fun toGeometry() = Geometry(
        coordinates = coordinates,
        type = type
    )
}