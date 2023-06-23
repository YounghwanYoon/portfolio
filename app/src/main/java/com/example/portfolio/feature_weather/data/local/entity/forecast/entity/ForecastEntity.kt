package com.example.portfolio.feature_weather.data.local.entity.forecast.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.portfolio.feature_weather.data.local.WeatherDataBase
import com.example.portfolio.feature_weather.data.utils.ForecastTypeConverter
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast

//@Entity(tableName = ForecastDataBase.DATABASE_NAME)
@Entity(tableName = "${WeatherDataBase.DATABASE_NAME}_forecast")
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @TypeConverters(ForecastTypeConverter::class)
    @ColumnInfo(name = "properties")
    var properties: PropertiesEntity,
){
    fun toForecast(): Forecast {
        return Forecast(
            properties = this.properties.toProperties()
        )
    }
}
