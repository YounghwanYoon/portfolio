package com.example.portfolio.feature_weather.data.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.PropertiesEntity
import com.example.portfolio.feature_weather.domain.model.forecasthourly.Properties
import com.example.portfolio.utils.JsonParser
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class ForecastHourlyTypeConverter (private val jsonParser: JsonParser){

    @TypeConverter
    fun fromPropertiesJson(json: String): PropertiesEntity?{
        val type = object: TypeToken<PropertiesEntity>(){}.type
        return jsonParser.fromJson(json, type)
    }

    @TypeConverter
    fun toPropertiesJson(properties:PropertiesEntity): String?{
        val type = object:TypeToken<PropertiesEntity>(){}.type
        return jsonParser.toJson(properties, type) ?:""
    }

}