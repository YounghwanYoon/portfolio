package com.example.portfolio.feature_weather.data.local.entity.forecast.entity

import androidx.room.PrimaryKey
import com.example.portfolio.feature_weather.domain.model.forecast.Elevation

data class ElevationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val unitCode: String,
    val value: Double
){
    fun toElevation() = Elevation(
        unitCode = this.unitCode,
        value = this.value
    )

}
