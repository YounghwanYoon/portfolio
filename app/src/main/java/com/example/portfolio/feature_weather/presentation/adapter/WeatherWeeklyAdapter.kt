package com.example.portfolio.feature_weather.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.databinding.WeatherWeeklyItemsBinding
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.utils.Helpers

class WeatherWeeklyAdapter:RecyclerView.Adapter<WeatherWeeklyAdapter.CustomViewModelHolder>() {
    private val TAG = "WeatherWeeklyAdapter"

    private var binding:WeatherWeeklyItemsBinding ?  = null
    private var data:Forecast? = null

    private var layoutInflater:LayoutInflater? = null

    inner class CustomViewModelHolder(val view: View):RecyclerView.ViewHolder(view){
        fun onBind(position:Int){
            Log.d(TAG, "onBind: size of Data ")

            val period = data?.properties?.periods?.get(position)
            val date = period?.startTime?.substringBefore("T")
            val day = date?.let { Helpers.getDayOfWeekByDate(it) }
            val shortForecast = period?.shortForecast
            val temperature = "${period?.temperature} F"//${period?.temperatureUnit}"

            binding!!.dateTxtview.text = date
            binding!!.dayTxtview.text = day
            binding!!.tempTxtview.text = temperature
            with(shortForecast){
                when{
                    this?.contains("Clear") == true || this?.contains("Sunny") == true -> {
                        binding!!.weatherImgview.setImageResource(R.drawable.ic_weather_sunny_svg)
                    }
                    this?.contains("Rain") == true || this?.contains("Showers") == true -> {
                        binding!!.weatherImgview.setImageResource(R.drawable.ic_weather_rain)
                    }
                    this?.contains("Snow") == true || this?.contains("Icy") == true -> {
                        binding!!.weatherImgview.setImageResource(R.drawable.ic_weather_snow)
                    }
                    this?.contains("Cloudy") == true -> {
                        binding!!.weatherImgview.setImageResource(R.drawable.ic_weather_cloudy)
                    }
                    else -> binding!!.weatherImgview.setImageResource(R.drawable.ic_weather_partially_sunny_cloudy)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: Forecast){
        data = newData
        Log.d(TAG, "updateData: total number of new data is ${newData.properties.periods.size}")
        Log.d(TAG, "updateData: name of data ${newData.properties.periods[0].name}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewModelHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.weather_weekly_items, parent, false)
        binding = WeatherWeeklyItemsBinding.bind(view)

        return CustomViewModelHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: CustomViewModelHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return 7 //because a week is 7 days
    }

}