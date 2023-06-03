package com.example.portfolio.feature_weather.data.local.entity.forecasthourly.entity

import androidx.room.PrimaryKey
import com.example.portfolio.feature_weather.domain.model.forecasthourly.Geometry

data class GeometryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    //val coordinates: List<List<List<Double>>>,
    val type: String
){
    fun toGeometry() = Geometry(
        //coordinates = coordinates,
        type = type
    )
}