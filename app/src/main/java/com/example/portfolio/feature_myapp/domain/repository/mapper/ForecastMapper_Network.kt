package com.example.portfolio.feature_myapp.domain.repository.mapper

import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastEntity
import com.example.portfolio.utils.EntityMapper
import javax.inject.Inject
/*
class ForecastMapperNetwork  constructor() : EntityMapper<Forecast, ForecastEntity> {
    override fun mapFrom(data: Forecast): ForecastEntity {
        return ForecastEntity(
            properties = data.properties
        )
    }

    override fun mapFromListOf(listData: List<Forecast>): List<ForecastEntity> {
        TODO("No Need for now.")
    }
}

class ForecastMapperLocal  constructor() : EntityMapper<ForecastEntity, Forecast> {
    override fun mapFrom(data: ForecastEntity): Forecast {
        return Forecast(
            properties = data.properties
        )


    }

    override fun mapFromListOf(listData: List<ForecastEntity>): List<Forecast> {
        TODO("Not yet implemented")
    }

}

 */