package com.example.portfolio.feature_weather.data.remote.dto.forecast_dto

import com.example.portfolio.feature_weather.data.local.entity.forecast.entity.PeriodEntity
import com.google.gson.annotations.SerializedName

data class PeriodDto(
    val detailedForecast: String,
    val endTime: String,
    val icon: String? = null,
    val isDaytime: Boolean,
    val name: String,
    val number: Int,
    val shortForecast: String,
    @SerializedName("startTime")
    val startTime: String,
    val temperature: Int,
    val temperatureTrend: Any,
    val temperatureUnit: String,
    val windDirection: String,
    val windSpeed: String
) {
    fun toPeriodEntity()= PeriodEntity(
        detailedForecast= this.detailedForecast,
        endTime= this.endTime,
        //icon= this.icon,
        isDaytime= this.isDaytime,
        name= this.name ,
        number= this.number,
        shortForecast= this.shortForecast,
        startTime= this.startTime,
        temperature= this.temperature,
        temperatureTrend= this.temperatureTrend,
        temperatureUnit= this.temperatureUnit,
        windDirection= this.windDirection,
        windSpeed= this.windSpeed
    )
}