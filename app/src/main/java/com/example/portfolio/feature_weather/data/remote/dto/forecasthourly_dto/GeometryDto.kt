package com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto

data class GeometryDto(
    //val coordinates: List<List<List<Double>>>,
    val type: String
){
    fun toGeometryEntity() = GeometryEntity(
        //coordinates = coordinates,
        type = type
    )
}