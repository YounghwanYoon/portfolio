package com.example.portfolio.feature_weather.presentation.helper

import android.util.Log
import com.example.portfolio.R
import com.example.portfolio.feature_weather.data.remote.dto.forecasthourly_dto.PeriodDto
import com.example.portfolio.feature_weather.domain.model.forecasthourly.Period
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    fun getYYDDHH():String{
        //"2022-10-05T02:00:00-07:00"
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-ddTHH"))
    }

    fun getDayOfWeek():String{
        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd")).toString()

        return LocalDate.parse(today).dayOfWeek.toString()
    }
    fun removeFormatPeriodHourly(period:Period?):String?{
        return period?.startTime
            ?.substringBefore(":00-")
            ?.substringAfter("T")
    }

    fun findStartingIndex(list:List<Period>, target:Int, start:Int, end:Int):Int{
        val mid = (end - start) / 2
        val midValue = list[mid].startTime
            ?.filter { it -> it.isDigit() }
            ?.subSequence(0,10)
            .toString().toInt()
        Log.d(TAG, "findStartingIndex: midValue $midValue, mCurrentYYMMDD: $target")

        if(midValue == target)
            return mid
/*        if(end - start <2 && midValue != target){
            return -1
        }*/

        if(midValue > target){
            return  findStartingIndex(list, target,start, mid)
        }else //midValue 202210080, mCurrentYYMMDD: 2022100505
            return findStartingIndex(list, target,mid, end)
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