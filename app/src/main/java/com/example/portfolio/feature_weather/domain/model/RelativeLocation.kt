package com.example.portfolio.feature_weather.domain.model

import com.example.portfolio.feature_weather.data.remote.dto.Properties

data class RelativeLocation(

    val type: String,
    val properties: Properties

)