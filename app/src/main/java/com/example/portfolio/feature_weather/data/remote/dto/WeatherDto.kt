package com.example.portfolio.feature_weather.data.remote.dto

import com.example.portfolio.feature_weather.domain.model.Weather
import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("@context")
    val context: List<Any>,
    @SerializedName("geometry")
    val geometry: GeometryDto,
    @SerializedName("id")
    val id: String,
    //need properties gridId and grid x and grid y
    @SerializedName("properties")
    val properties: PropertiesDto,
    @SerializedName("type")
    val type: String
){
    fun toWeather(): Weather {
       return Weather(
            properties = properties.toProperty()
        )
    }

}