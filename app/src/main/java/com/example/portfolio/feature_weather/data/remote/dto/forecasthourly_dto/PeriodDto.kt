package com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto

data class PeriodDto(
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
)