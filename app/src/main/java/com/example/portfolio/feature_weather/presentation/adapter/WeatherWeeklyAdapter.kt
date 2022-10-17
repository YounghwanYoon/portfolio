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
import com.example.portfolio.feature_weather.domain.model.forecast.Period
import com.example.portfolio.feature_weather.presentation.helper.WeatherHelper

class WeatherWeeklyAdapter:RecyclerView.Adapter<WeatherWeeklyAdapter.CustomViewModelHolder>() {

    companion object {
        private const val TAG = "WeatherWeeklyAdapter"
    }

    private var binding: WeatherWeeklyItemsBinding?  = null
    private var data:Forecast? = null
    private var totalDaysToDisplay = 7

    private var customData:List<CustomData> = List<CustomData>(7 ){
        CustomData()
    }

    inner class CustomViewModelHolder(val view: View):RecyclerView.ViewHolder(view){
        fun onBind(data:CustomData){
            Log.d(TAG, "onBind: ${data.date}")

            binding!!.apply{
                this.dateTxt.text = data.date
                this.dayofweekTxt.text = data.dayOfWeek
                this.tempDayTxt.text = data.dayTemp.toString()
                data.dayShortForecast?.let{
                    this.weatherDayImg.setImageResource(
                        WeatherHelper.selectImage(it)
                    )
                }

                this.tempNightTxt.text = data.nightTemp.toString()
                data.nightShortForecast?.let{
                    this.weatherNightImg.setImageResource(
                        WeatherHelper.selectImage(it)
                    )
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateWeeklyForecast(newData: List<CustomData>){

        customData = newData
        newData.forEach{
            it.isDataAvailable = true
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewModelHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.weather_weekly_items, parent, false)
        binding = WeatherWeeklyItemsBinding.bind(view)

        return CustomViewModelHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: CustomViewModelHolder, position: Int) {
        if(customData[position].isDataAvailable){
            customData.let{
                holder.onBind(it[position])
            }
        }

    }


    override fun getItemCount(): Int {
        return customData.size-1 //because a week is 7 days
    }

}
data class CustomData(
    var isDataAvailable: Boolean= false,
    var date:String ? = null,
    var dayOfWeek:String ? = null,
    var dayTemp:Int? = null,
    var dayShortForecast:String ?=null,
    var nightTemp:Int ? = null,
    var nightShortForecast:String ?= null,
)
