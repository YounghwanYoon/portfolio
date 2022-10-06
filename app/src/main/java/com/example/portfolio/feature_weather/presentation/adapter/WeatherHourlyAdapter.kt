package com.example.portfolio.feature_weather.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.databinding.WeatherDetailItemsBinding
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import com.example.portfolio.feature_weather.domain.model.forecasthourly.Period
import com.example.portfolio.feature_weather.presentation.helper.WeatherHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherHourlyAdapter:RecyclerView.Adapter<WeatherHourlyAdapter.CustomViewModelHolder>() {
    private val TAG = "WeatherWeeklyAdapter"

    private var binding:WeatherDetailItemsBinding ?  = null
    //private var data:Forecast? = null
    private var mDataHourly:ForecastHourly? = null
    private var layoutInflater:LayoutInflater? = null
    private val mCurrentYYMMDD:Int = WeatherHelper.getCurrentYYMMDD().toInt()
    private val mCurrentTime:String = WeatherHelper.getCurrentTime()

    private fun limitPeriods(periods: List<Period>): List<Period> {
        periods.forEach{
            Log.d(TAG, "limitPeriods: ${it.startTime}")
        }
        
        val startingIndex = WeatherHelper.findStartingIndex(periods, mCurrentYYMMDD, 0, periods.size - 1)

        return periods.slice(startingIndex..startingIndex + 23)

    }

    inner class CustomViewModelHolder(val view: View):RecyclerView.ViewHolder(view){

        fun onBind(position: Int, limitPeriods: List<Period>?){
            Log.d(TAG, "onBind: size of Data ")

            //val period = mDataHourly?.properties?.periods?.get(position)
            val period = limitPeriods?.get(position)
            //"2022-10-05T02:00:00-07:00"
/*            val startTime = period?.startTime
                ?.substringBefore(":00-")
                ?.substringAfter("T")
*/
            val startTime= WeatherHelper.removeFormatPeriodHourly(period)
            val shortForecast = period?.shortForecast
            val temperature = "${period?.temperature} \u2109"
            
            //binding!!.dayTxtview.text = day
            binding?.apply{
                //forecastTempTxt.text = temperature

                forecastHourTxt.text = startTime
                shortForecast?.let{
                    forecastHourImg.setImageResource(WeatherHelper.selectImage(it))
                }
            }
          /*  with(shortForecast){
                when{
                    this?.contains("Clear") == true || this?.contains("Sunny") == true -> {
                        binding!!.forecastHourImg.setImageResource(R.drawable.ic_weather_sunny_svg)
                    }
                    this?.contains("Rain") == true || this?.contains("Showers") == true -> {
                        binding!!.forecastHourImg.setImageResource(R.drawable.ic_weather_rain)
                    }
                    this?.contains("Snow") == true || this?.contains("Icy") == true -> {
                        binding!!.forecastHourImg.setImageResource(R.drawable.ic_weather_snow)
                    }
                    this?.contains("Cloudy") == true -> {
                        binding!!.forecastHourImg.setImageResource(R.drawable.ic_weather_cloudy)
                    }
                    else -> binding!!.forecastHourImg.setImageResource(R.drawable.ic_weather_partially_sunny_cloudy)
                }
            }*/
        }
    }
/*
    @SuppressLint("NotifyDataSetChanged")
    fun updateWeeklyForecast(newData: Forecast){
        data = newData
        Log.d(TAG, "updateData: total number of new data is ${newData.properties.periods?.size}")
        notifyDataSetChanged()
    }
*/


    @SuppressLint("NotifyDataSetChanged")
    fun updateHourlyForecast(newData:ForecastHourly){
        mDataHourly = newData
        Log.d(TAG, "updateData: total number of new data is ${newData.properties.periods.size}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewModelHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.weather_detail_items, parent, false)
        binding = WeatherDetailItemsBinding.bind(view)

        return CustomViewModelHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: CustomViewModelHolder, position: Int) {
        holder.onBind(position, mDataHourly?.properties?.periods?.let { limitPeriods(it) })
    }

    override fun getItemCount(): Int {
        return 24 //because a week is 7 days
    }

}