package com.example.portfolio.feature_weather.data.local.entity.forecasthourly

import com.example.portfolio.feature_weather.domain.model.forecasthourly.Period

data class PeriodEntity(
    val detailedForecast: String,
    val endTime: String,
    val icon: String,
    val isDaytime: Boolean,
    val name: String,
    val number: Int,
    val shortForecast: String,
    val startTime: String,
    val temperature: Int,
    val temperatureTrend: Any,
    val temperatureUnit: String,
    val windDirection: String,
    val windSpeed: String
){
    fun toPeriod () = Period(
        detailedForecast = detailedForecast,
        endTime = endTime,
        icon = icon,
        isDaytime = isDaytime,
        name = name,
        number = number,
        shortForecast = shortForecast,
        startTime = startTime,
        temperature = temperature,
        temperatureTrend = temperatureTrend,
        temperatureUnit = temperatureUnit,
        windDirection = windDirection,
        windSpeed = windSpeed
    )
}