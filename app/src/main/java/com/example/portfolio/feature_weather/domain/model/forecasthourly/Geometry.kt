package com.example.portfolio.feature_weather.domain.model.forecasthourly

data class Geometry(
    val coordinates: List<List<List<Double>>>,
    val type: String
)