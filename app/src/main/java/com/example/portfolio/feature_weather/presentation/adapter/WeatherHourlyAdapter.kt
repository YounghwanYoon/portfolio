package com.example.portfolio.feature_weather.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.databinding.WeatherDetailItemsBinding
import com.example.portfolio.databinding.WeatherWeeklyItemsBinding
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.domain.model.forecasthourly.ForecastHourly
import com.example.portfolio.utils.Helpers
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherHourlyAdapter:RecyclerView.Adapter<WeatherHourlyAdapter.CustomViewModelHolder>() {
    private val TAG = "WeatherWeeklyAdapter"

    private var binding:WeatherDetailItemsBinding ?  = null
    //private var data:Forecast? = null
    private var dataHourly:ForecastHourly? = null

    private val currentTime = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("HH::mm")
    )

    private var layoutInflater:LayoutInflater? = null

    inner class CustomViewModelHolder(val view: View):RecyclerView.ViewHolder(view){
        fun onBind(position:Int){
            Log.d(TAG, "onBind: size of Data ")

            val period = dataHourly?.properties?.periods?.get(position)
            val startTime = period?.startTime?.substringBefore("T")
            val shortForecast = period?.shortForecast
            val temperature = "${period?.temperature} \u2109"
            
            //binding!!.dayTxtview.text = day
            binding?.apply{
                forecastTempTxt.text = temperature
                forecastHourTxt.text = startTime
            }
            with(shortForecast){
                when{
                    this?.contains("Clear") == true || this?.contains("Sunny") == true -> {
                        binding!!.forecastHourlyImg.setImageResource(R.drawable.ic_weather_sunny_svg)
                    }
                    this?.contains("Rain") == true || this?.contains("Showers") == true -> {
                        binding!!.forecastHourlyImg.setImageResource(R.drawable.ic_weather_rain)
                    }
                    this?.contains("Snow") == true || this?.contains("Icy") == true -> {
                        binding!!.forecastHourlyImg.setImageResource(R.drawable.ic_weather_snow)
                    }
                    this?.contains("Cloudy") == true -> {
                        binding!!.forecastHourlyImg.setImageResource(R.drawable.ic_weather_cloudy)
                    }
                    else -> binding!!.forecastHourlyImg.setImageResource(R.drawable.ic_weather_partially_sunny_cloudy)
                }
            }
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
        dataHourly = newData
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
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return 24 //because a week is 7 days
    }

}