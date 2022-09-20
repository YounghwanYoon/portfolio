package com.example.portfolio.feature_weather.data.local.entity.forecasthourly

import com.example.portfolio.feature_weather.domain.model.forecasthourly.Elevation

data class ElevationEntity(
    val unitCode: String,
    val value: Double
){
    fun toElevation(): Elevation  {
        return Elevation(
            unitCode = this.unitCode,
            value = this.value
        )
    }

}