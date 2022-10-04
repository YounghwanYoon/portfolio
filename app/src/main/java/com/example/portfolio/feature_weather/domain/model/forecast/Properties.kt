package com.example.portfolio.feature_weather.domain.model.forecast

data class Properties(
    val elevation: Elevation? = null,
//    val forecastGenerator: String? = null,
//    val generatedAt: String? = null,
//    //This contains list of temperature data.
   val periods: List<Period>? = null,
//    val units: String? = null,
    val updateTime: String? = null,
//    val updated: String? = null,
//    val validTimes: String? = null
){

}