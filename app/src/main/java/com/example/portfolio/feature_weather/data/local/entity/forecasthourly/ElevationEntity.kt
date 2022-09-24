package com.example.portfolio.feature_weather.data.local.entity.forecasthourly

import androidx.room.PrimaryKey
import com.example.portfolio.feature_weather.domain.model.forecasthourly.Elevation

data class ElevationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
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