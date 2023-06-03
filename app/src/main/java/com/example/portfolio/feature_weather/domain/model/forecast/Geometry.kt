package com.example.portfolio.feature_weather.domain.model.forecast

data class Geometry(
    val coordinates: List<List<List<Double>>>,
    val type: String
)