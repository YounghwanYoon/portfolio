package com.example.portfolio.feature_weather.data.remote.dto

import com.example.portfolio.feature_weather.domain.model.Properties
import com.example.portfolio.feature_weather.domain.model.RelativeLocation

data class PropertiesDto(
    val county: String,
    val cwa: String,
    val fireWeatherZone: String,
    val forecast: String,
    val forecastGridData: String,
    val forecastHourly: String,
    val forecastOffice: String,
    val forecastZone: String,
    val gridId: String,
    val gridX: Int,
    val gridY: Int,
    val observationStations: String,
    val radarStation: String,
    val relativeLocation: RelativeLocation,
    val timeZone: String
){

    fun toProperty(): Properties {
        return Properties(
            forecast = forecast,
            forecastHourly = forecastHourly,
            timeZone =  timeZone,
            forecastOffice = forecastOffice,
            gridId = gridId,
            gridX = gridX,
            gridY = gridY,
            relativeLocation = relativeLocation
        )
    }
}