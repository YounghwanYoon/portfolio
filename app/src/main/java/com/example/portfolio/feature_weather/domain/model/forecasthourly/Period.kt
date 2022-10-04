package com.example.portfolio.feature_weather.domain.model.forecasthourly

data class Period(
    val detailedForecast: String? = null,
    val endTime: String? = null,
    val icon: String? = null,
    val isDaytime: Boolean? = null,
    val name: String? = null,
    val number: Int? = null,
    val shortForecast: String? = null,
    val startTime: String? = null,
    val temperature: Int? = null,
    val temperatureTrend: Any? = null,
    val temperatureUnit: String? = null,
    val windDirection: String? = null,
    val windSpeed: String? = null
)