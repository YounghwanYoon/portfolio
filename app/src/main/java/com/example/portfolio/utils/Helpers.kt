package com.example.portfolio.utils

import android.util.Log
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.repository.WeatherServices
import com.example.portfolio.feature_weather.presentation.WeatherFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

object Helpers {
    fun numberFormatDouble(double:Double):Double{
        return DecimalFormat("##.0000").format(double).toDouble()
    }

    //return day of week of given date, with yyyy-mm-dd format.
    fun getDayOfWeekByDate(date:String):String{
        val cal = Calendar.getInstance()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("u-M-d", Locale.ENGLISH)
        return LocalDate.parse(date,dateTimeFormatter).dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
    }

}