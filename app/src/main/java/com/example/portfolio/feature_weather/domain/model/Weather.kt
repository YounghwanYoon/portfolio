package com.example.portfolio.feature_weather.domain.model

import com.google.gson.annotations.SerializedName

data class Weather(
//    @SerializedName("@context")
//    val context: List<Any>,
//    val geometry: Geometry,
//    val id: String,
    //need properties gridId and grid x and grid y
    val properties: Properties,
//    val type: String
)