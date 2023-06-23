package com.example.portfolio.feature_myapp.domain.repository.mapper

import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.data.local.entity.weather.WeatherEntity
import com.example.portfolio.utils.EntityMapper
import javax.inject.Inject

class WeatherMapper_Network @Inject constructor(): EntityMapper<Weather, WeatherEntity> {
    override fun mapFrom(data: Weather): WeatherEntity {
        TODO("Not yet implemented")
    }

    override fun mapFromListOf(listData: List<Weather>): List<WeatherEntity> {
        TODO("Not yet implemented")
    }
}

class WeatherMapper_Local @Inject constructor(): EntityMapper<WeatherEntity, Weather> {
    override fun mapFrom(data: WeatherEntity): Weather {
        TODO("Not yet implemented")
    }

    override fun mapFromListOf(listData: List<WeatherEntity>): List<Weather> {
        TODO("Not yet implemented")
    }
}

