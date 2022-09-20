package com.example.portfolio.feature_weather.domain.model.forecasthourly

import com.google.gson.annotations.SerializedName

data class ForecastHourly(
    @SerializedName("@context")
    val context: List<Any>,
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)