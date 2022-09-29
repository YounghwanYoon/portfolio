package com.example.portfolio.feature_weather.data.local.entity.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.portfolio.feature_weather.data.local.WeatherDataBase
import com.example.portfolio.feature_weather.domain.model.Properties
import com.example.portfolio.feature_weather.data.utils.WeatherTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = WeatherDataBase.DATABASE_NAME)
data class WeatherEntity(
    @PrimaryKey
    val id: Int,
    @TypeConverters(WeatherTypeConverter::class)
    @ColumnInfo(name = "properties")
    @SerializedName("properties")
    var properties: Properties,
)

