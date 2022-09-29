package com.example.portfolio.feature_weather.data.local.entity.forecasthourly.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.portfolio.feature_weather.data.local.WeatherDataBase
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly

@Entity(tableName = "${WeatherDataBase.DATABASE_NAME}_forecast_hourly")
data class ForecastHourlyEntity(
    //SerializedName("@context")
    //val context: List<Any>,
    //val geometry: GeometryEntity,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val properties: PropertiesEntity,
    val type: String
){
    fun toForecastHourly() = ForecastHourly(
        //context= context,
        //geometry = geometry.toGeometry(),
        properties = properties.toProperties(),
        type = type

    )
}