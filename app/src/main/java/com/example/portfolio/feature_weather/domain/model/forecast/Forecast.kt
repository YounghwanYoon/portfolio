package com.example.portfolio.feature_weather.domain.model.forecast

import com.google.gson.annotations.SerializedName


data class Forecast(
    //val geometry: Geometry,
    @SerializedName("properties")
    val properties: Properties,
    //val type: String
){

}