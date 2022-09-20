package com.example.portfolio.feature_weather.data.local.entity.forecast

import com.example.portfolio.feature_weather.domain.model.forecast.Elevation

data class ElevationEntity(
    val unitCode: String,
    val value: Double
){
    fun toElevation() = Elevation(
        unitCode = this.unitCode,
        value = this.value
    )

}
