package com.example.portfolio.feature_weather.data.local.entity.forecast.entity

import androidx.room.PrimaryKey
import com.example.portfolio.feature_weather.domain.model.forecast.Period

data class PeriodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val detailedForecast: String,
    val endTime: String,
    //val icon: String,
    val isDaytime: Boolean,
    val name: String,
    val number: Int,
    val shortForecast: String,
    val startTime: String?,
    val temperature: Int,
    val temperatureTrend: Any?=null,
    val temperatureUnit: String?=null,
    val windDirection: String?=null,
    val windSpeed: String?=null
){
    fun toPeriod() = Period(
        detailedForecast= this.detailedForecast,
        endTime= this.endTime,
        //icon= this.icon,
        isDaytime= this.isDaytime,
        name= this.name ,
        number= this.number,
        shortForecast= this.shortForecast,
        startTime= this.startTime,
        temperature= this.temperature,
        //temperatureTrend= this.temperatureTrend,
        //temperatureUnit= this.temperatureUnit,
        windDirection= this.windDirection,
        windSpeed= this.windSpeed
    )

}