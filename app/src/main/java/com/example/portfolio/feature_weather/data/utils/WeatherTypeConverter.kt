package com.example.portfolio.feature_weather.data.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.portfolio.feature_weather.domain.model.Properties
import com.example.portfolio.utils.JsonParser
import com.google.gson.reflect.TypeToken


@ProvidedTypeConverter
class WeatherTypeConverter(private val jsonParser: JsonParser) {

    @TypeConverter
    fun fromPropertiesJson(json: String):Properties?{
        val type = object:TypeToken<Properties>(){}.type
        return jsonParser.fromJson(json,type)
    }

    @TypeConverter
    fun toPropertiesJson(properties:Properties): String? {
        val type = object: TypeToken<Properties>(){}.type
        return jsonParser.toJson(properties, type) ?:""
    }

}