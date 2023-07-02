package com.example.portfolio.feature_myapp.domain.repository.mapper

import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.data.local.entity.weather.WeatherEntity
import com.example.portfolio.feature_shopping.data.local.util.NetworkMapper
import javax.inject.Inject

class WeatherMapper_Network @Inject constructor(): NetworkMapper<Weather, WeatherEntity> {
    override fun mapFromDTO(data: Weather): WeatherEntity {
        TODO("Not yet implemented")
    }

    override fun mapFromListDTO(listData: List<Weather>): List<WeatherEntity> {
        TODO("Not yet implemented")
    }
}

class WeatherMapper_Local @Inject constructor(): NetworkMapper<WeatherEntity, Weather> {
    override fun mapFromDTO(data: WeatherEntity): Weather {
        TODO("Not yet implemented")
    }

    override fun mapFromListDTO(listData: List<WeatherEntity>): List<Weather> {
        TODO("Not yet implemented")
    }
}

