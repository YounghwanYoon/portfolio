package com.example.portfolio.feature_weather.presentation.helper

import android.annotation.SuppressLint
import android.util.Log
import com.example.portfolio.feature_weather.domain.model.forecasthourly.Period
import com.example.portfolio.feature_weather.presentation.adapter.CustomData
import com.google.android.material.timepicker.TimeFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object WeatherHelper {
    private const val TAG = "WeatherHelper"


    //chart reference
    //https://www.baeldung.com/java-datetimeformatter
    fun getCurrentTime():String{
        val currentDate = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("uuuu-MM")
        )
        val divider = "T"
        val hour = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("HH:")
        )
        return currentDate+divider+hour+"00"
    }
    fun getCurrentYYMMDD():String{
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMddHH"))
    }
    fun getYYMMDD():String{
        //"2022-10-05T02:00:00-07:00"
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-ddTHH"))
    }

    @SuppressLint("SimpleDateFormat")
    fun changeTo12HourFormat(data:String):String{
        var result:String? = null
        try{
            val sdf = SimpleDateFormat("H:mm")
            val dateObj = sdf.parse(data)
            result = SimpleDateFormat("hh:mm aa").format(dateObj)
        }catch (e:ParseException){
            e.printStackTrace()
        }

        return result ?: ""

    }


    fun geDayOfWeekOfToday():String{
        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd")).toString()

        return LocalDate.parse(today).dayOfWeek.toString()
    }



    fun geDayOfWeekOfToday(date:String):String{
        return LocalDate.parse(date).dayOfWeek.toString()
    }

    fun removeFormatPeriodHourly(startTime:String):String{
        return startTime
            .substringBefore(":00-")
            .substringAfter("T")
    }

    fun findStartingIndex(list:List<Period>, target:Int, start:Int, end:Int):Int{
        val mid = (start + end) / 2
        val midValue = list[mid].startTime
            ?.filter { it -> it.isDigit() }
            ?.subSequence(0,10)
            .toString().toInt()

        Log.d(TAG, "findStartingIndex: midValue $midValue, mCurrentYYMMDD: $target")

        if(midValue == target)
            return mid

        if(midValue > target){
            return  findStartingIndex(list, target,start, mid)
        }else //mid value      2022100702,
            // mCurrentYYMMDD: 2022100704
            return findStartingIndex(list, target,mid+1, end)
    }
    fun sliceForecastHourlyList(givenList: List<Period>, startingIndex:Int, totalHoursToDisplay:Int): List<Period> {
        return givenList.slice(startingIndex until (startingIndex + totalHoursToDisplay))
    }

    fun filterForecast(list: List<com.example.portfolio.feature_weather.domain.model.forecast.Period>,
                       totalDaysToDisplay: Int ): List<CustomData> {
        Log.d(TAG, "filterForecast: ${list.size}")
        val newList = List<CustomData>(totalDaysToDisplay){ CustomData() }

        var index = 0
        var i =0

        while(i < list.size){
            list[i].apply{
                if(!this.name.contains("Overnight") && index < totalDaysToDisplay){
                    if(this.isDaytime){
                        newList[index].dayTemp = this.temperature
                        newList[index].dayShortForecast = this.shortForecast
                        newList[index].date = this.startTime!!.substringBefore("T")//this.startTime!!.substringBefore(":00-").substringAfter("T"),
                        newList[index].dayOfWeek = WeatherHelper.geDayOfWeekOfToday(this.startTime.substringBefore("T"))
                    }
                    if(!this.isDaytime && newList[index].dayTemp != null){
                        newList[index].nightTemp = this.temperature
                        newList[index].nightShortForecast = this.shortForecast
                        index++
                    }
                }
            }
            i++
        }

        return newList
    }


    fun findTodayData(list:List<Period>):Period{
        val target = getCurrentYYMMDD().toInt()
        val targetIndex = findStartingIndex(list, target, 0, list.size -1)
        return list[targetIndex]
    }

    fun selectImage(imageInfo:String):Int{
        with(imageInfo){
            return when{
                this.contains("Clear") || this.contains("Sunny") -> {
                    com.example.portfolio.R.drawable.ic_weather_sunny_svg
                }
                this.contains("Rain") || this.contains("Showers") -> {
                    com.example.portfolio.R.drawable.ic_weather_rain
                }
                this.contains("Snow") || this.contains("Icy") -> {
                    com.example.portfolio.R.drawable.ic_weather_snow
                }
                this.contains("Cloudy") -> {
                    com.example.portfolio.R.drawable.ic_weather_cloudy
                }
                else -> com.example.portfolio.R.drawable.ic_weather_partially_sunny_cloudy
            }
        }

    }

}