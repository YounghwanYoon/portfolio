package com.example.portfolio.feature_weather.data.local.entity.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.portfolio.feature_weather.data.local.WeatherDataBase
import com.example.portfolio.feature_weather.data.utils.ForecastTypeConverter
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.domain.model.forecast.Properties
import com.google.gson.annotations.SerializedName

//@Entity(tableName = ForecastDataBase.DATABASE_NAME)
@Entity(tableName = "${WeatherDataBase.DATABASE_NAME}_forecast")
data class ForecastEntity(
    @TypeConverters(ForecastTypeConverter::class)
    @SerializedName("Properties")
    var properties: PropertiesEntity,
){
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    fun toForecast(): Forecast {
        return Forecast(
            properties = this.properties.toProperties()
        )
    }
}
