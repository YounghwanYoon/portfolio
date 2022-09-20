package com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto

import com.example.portfolio.feature_weather.data.local.entity.forecasthourly.PeriodEntity

data class properties(
    val elevationDto: ElevationDto,
    val forecastGenerator: String,
    val generatedAt: String,
    val periodDtos: List<PeriodEntity>,
    val units: String,
    val updateTime: String,
    val updated: String,
    val validTimes: String
)