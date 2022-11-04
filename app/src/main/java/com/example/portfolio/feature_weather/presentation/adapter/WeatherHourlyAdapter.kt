package com.example.portfolio.feature_weather.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.databinding.WeatherDetailItemsBinding
import com.example.portfolio.feature_weather.domain.model.forecasthourly.Period
import com.example.portfolio.feature_weather.presentation.helper.WeatherHelper
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
class WeatherHourlyAdapter constructor():RecyclerView.Adapter<WeatherHourlyAdapter.CustomViewModelHolder>() {
    private val TAG = "WeatherWeeklyAdapter"

    private var binding:WeatherDetailItemsBinding ?  = null
    private val _12HourSDF = SimpleDateFormat("HH:mm a")
    private var totalHoursToDisplay = 24
    private var data :List<Period>?  = List(totalHoursToDisplay){
        Period()
    }


    inner class CustomViewModelHolder(val view: View):RecyclerView.ViewHolder(view){

        fun onBind(startTime:String, shortForecast:String, temperature:String){
            //binding!!.dayTxtview.text = day
/*            selectedData.apply{

                binding!!.forecastHourTxt.text = WeatherHelper.changeTo12HourFormat((this.startTime!!))
                this.shortForecast!!.let{
                    binding!!.forecastHourImg.setImageResource(WeatherHelper.selectImage(it))
                }
                binding!!.forecastHourlyTemp.text = this.temperature.toString()

            }*/

            binding?.apply{
                forecastHourTxt.text = WeatherHelper.changeTo12HourFormat((startTime))
                shortForecast.let{
                    forecastHourImg.setImageResource(WeatherHelper.selectWeatherImage(it))
                }
                forecastHourlyTemp.text = temperature
            }
        }
    }
    private fun printData(data:String){
        Log.d(TAG, "printData: $data")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateHourlyForecast(newData:List<Period>){
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewModelHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.weather_detail_items, parent, false)
        binding = WeatherDetailItemsBinding.bind(view)

        return CustomViewModelHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: CustomViewModelHolder, position: Int) {
            if(data?.get(0)?.startTime != null){
                data?.let{
                    it[position].apply{
                        holder.onBind(
                            WeatherHelper.removeFormatPeriodHourly(this.startTime!!),
                            this.shortForecast!!,
                            "${this.temperature} \u2109")
                    }
                }
            }
    }

    //it stopped displaying data with wrong position
    //however, it should be avoid when displaying bigger data for performance.
    override fun getItemViewType(position: Int): Int {
        //return super.getItemViewType(position)
        return position
    }

    override fun getItemCount(): Int {
        return data!!.size // better use data.size or display delay happens
    }

}