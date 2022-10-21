package com.example.portfolio.feature_weather.presentation.`interface`

interface CustomLocationListener {

    fun onSuccessLocationPermission(result:Boolean)

    fun onFailedGettingLocationPermission(result:Boolean)


}