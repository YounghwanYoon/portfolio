package com.example.portfolio.feature_weather.domain.model.forecast

import com.example.portfolio.feature_weather.data.local.entity.forecast.PropertiesEntity

data class Properties(
    val elevation: Elevation,
//    val forecastGenerator: String,
//    val generatedAt: String,
//    //This contains list of temperature data.
   val periods: List<Period>,
//    val units: String,
//    val updateTime: String,
//    val updated: String,
//    val validTimes: String
){

}