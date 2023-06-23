package com.example.portfolio.feature_weather.domain.model

data class Properties(
//    val county: String,
//    val cwa: String,
//    val fireWeatherZone: String,
    val forecast: String,
//    val forecastGridData: String,
    val forecastHourly: String,
    val forecastOffice: String,
//    val forecastZone: String,
    val gridId: String,
    val gridX: Int,
    val gridY: Int,
//    val observationStations: String,
//    val radarStation: String,
    val relativeLocation: RelativeLocation,
    val timeZone: String
)