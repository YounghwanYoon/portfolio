package com.example.portfolio.feature_weather.presentation



import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portfolio.R
import com.example.portfolio.databinding.FragmentWeatherBinding
import com.example.portfolio.feature_weather.domain.model.forecasthourly.Period
import com.example.portfolio.feature_weather.presentation.adapter.CustomData
import com.example.portfolio.feature_weather.presentation.adapter.WeatherHourlyAdapter
import com.example.portfolio.feature_weather.presentation.adapter.WeatherWeeklyAdapter
import com.example.portfolio.feature_weather.presentation.helper.WeatherHelper
import com.example.portfolio.utils.DataState
import com.example.portfolio.utils.LocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.withContext


class WeatherFragment : Fragment(R.layout.fragment_weather){
    private lateinit var locationService:LocationService
    private val vm: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentWeatherBinding
    private lateinit var weeklyAdapter: WeatherWeeklyAdapter
    private lateinit var hourlyAdapter: WeatherHourlyAdapter

    companion object {
        private const val TAG = "WeatherFragment"
    }

    //lifecycle relate
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWeatherBinding.inflate(inflater,container, false)
        val view = binding.root
        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        locationHelper()
        vm.setPermState(PermissionState.Requesting)
        setWeather(requireContext())
        //initRecyclerview()
        //setUpForecastRecyclerView()
        //setUpHourlyRecycleView()
        subscribeVM()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")



        super.onResume()
    }

    private fun toggleProgressBarTop(viewType:Int){
        binding.progressBarTop.visibility = viewType
    }
    private fun toggleProgressBarBtm(viewType:Int){
      binding.progressBarBtm.visibility = viewType
    }

    private fun initRecyclerview(){
        setUpForecastRecyclerView()
        setUpHourlyRecycleView()
    }
    private fun setUpForecastRecyclerView(){

        binding.forecastWeeklyRecyclerview.apply{
            weeklyAdapter = WeatherWeeklyAdapter()

            this.setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = weeklyAdapter
        }
    }
    private fun setUpHourlyRecycleView(){
        binding.forecastHourlyRecyclerview.apply{
            this.setHasFixedSize(true)
            hourlyAdapter = WeatherHourlyAdapter()

            this.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = hourlyAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpView(period:Period){
        binding.apply{
            period.shortForecast?.let{
                currentHourImage.setImageResource( WeatherHelper.selectImage(it))
            }
            currentHourTempTxt.text = ("${period.temperature} \u2109")
            currentDayOfWeekTxt.text = WeatherHelper.geDayOfWeekOfToday()
        }
    }
    private fun setCurrentLocationView(cityState:String){
        binding.apply{
            currentLocationTxt.text = cityState
        }
    }


    //view model relate
    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeVM(){
        //must be separate lifecycleScope or data will not be updated.
        //but why?
        viewLifecycleOwner.lifecycleScope.launchWhenStarted{
            vm.weatherState.collect{ dataState->
                when(dataState){
                    is DataState.Error -> {
                        Log.d(TAG, "subscribeVM: Error calling weatherState")
                    }
                    is DataState.Loading -> {
                        Log.d(TAG, "subscribeVM: weatherState is loading")
                    }
                    is DataState.Success -> {
                        Log.d(TAG, "subscribeVM: Weather Request is Success")
                        dataState.data?.properties?.relativeLocation?.properties?.let{ location ->
                            setCurrentLocationView("${location.city}, ${location.state}")
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.forecastHourlyState
                .collect{ data ->
                    delay(1000)
                    if(data!= null){
                        Log.d(TAG, "subscribeVM: updating forecast hourly data to adapter")
                        withContext(Dispatchers.Main){
                            val startingIndex:Int
                            val forecastHourlyList: List<Period>
                            val totalHoursToDisplay = 24
                            data.properties.apply{
                                startingIndex = WeatherHelper.findStartingIndex(this.periods,
                                    WeatherHelper.getCurrentYYMMDD().toInt(),
                                    0,
                                    this.periods.size-1
                                )
                                forecastHourlyList = WeatherHelper.sliceForecastHourlyList(this.periods, startingIndex, totalHoursToDisplay)
                                setUpView(WeatherHelper.findTodayData(this.periods))
                            }

                            Log.d(TAG, "subscribeVM: forecastHourlyList - ${forecastHourlyList.size}")
                            toggleProgressBarTop(View.GONE)
                            setUpHourlyRecycleView()
                            hourlyAdapter.updateHourlyForecast(forecastHourlyList)
                        }

                    }
                }
        }
/*

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            vm.forecastState
                .distinctUntilChangedBy {  }
                .collect { data ->
                    if (data != null) {
                        Log.d(TAG, "subscribeVM: forecast data is ${data.properties.updateTime}")
                        Log.d(TAG, "subscribeVM: updating forecast to adapter")

                        toggleProgressBarBtm(View.GONE)

                        val totalDaysToDisplay = 7
                        val customData: List<CustomData>

                        data.properties.periods?.let {
                            customData = WeatherHelper.filterForecast(it, totalDaysToDisplay)
                            setUpForecastRecyclerView()
                            weeklyAdapter.updateWeeklyForecast(customData)
                        }
                    }
            }
        }
*/
/*
        if(vm._forecastListState.value != emptyList<Period>()){

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                vm.forecastListState
                    .distinctUntilChangedBy {  }
                    .collectLatest { data ->
                        if (data != null) {
                            Log.d(TAG, "subscribeVM: forecast data is ${data[0].startTime}")
                            Log.d(TAG, "subscribeVM: updating forecast to adapter")

                            toggleProgressBarBtm(View.GONE)

                            val totalDaysToDisplay = 7
                            val customData: List<CustomData> = WeatherHelper.filterForecast(data, totalDaysToDisplay)
                            setUpForecastRecyclerView()
                            weeklyAdapter.updateWeeklyForecast(customData)

                        }else
                            Log.d(TAG, "subscribeVM: data is empty for forecast")
                    }
            }
        }
        */
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.forecastListState
                .distinctUntilChangedBy {  }
                .collectLatest { data ->
                    if (data != null) {
                        Log.d(TAG, "subscribeVM: forecast data is ${data[0].startTime}")
                        Log.d(TAG, "subscribeVM: updating forecast to adapter")

                        toggleProgressBarBtm(View.GONE)

                        val totalDaysToDisplay = 7
                        val customData: List<CustomData> = WeatherHelper.filterForecast(data, totalDaysToDisplay)
                        setUpForecastRecyclerView()
                        weeklyAdapter.updateWeeklyForecast(customData)

                    }else
                        Log.d(TAG, "subscribeVM: data is empty for forecast")
                }
        }

/*

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.forecastState.collect{ state ->
                when(state){
                    is DataState.Error -> TODO()
                    is DataState.Loading -> TODO()
                    is DataState.Success -> {
                        withContext(Dispatchers.Main) {
                            toggleProgressBarBtm(View.GONE)

                            val totalDaysToDisplay = 7
                            val customData: List<CustomData>

                            state.data?.properties?.periods?.let{
                                customData = WeatherHelper.filterForecast(it, totalDaysToDisplay)
                                setUpForecastRecyclerView()
                                weeklyAdapter.updateWeeklyForecast(customData)
                            }
                        }
                    }
                }

            }
        }
*/



    }
    private fun setWeather(context:Context){
        var permState: PermissionState = PermissionState.Requesting
        permState = if(isPermissionGranted(context)){
            PermissionState.Granted(getCurrentGPS())
        }else{
            PermissionState.Error("Permission Error")
        }

        vm.setPermState(permState)
    }

    // Location Permission and GPS related
    //receive either fragment or activity
    private fun locationHelper(){
        initLocationService(this)
        getCurrentGPS()
    }
    private fun initLocationService(fragment: Fragment){
        locationService = LocationService(fragment = fragment)
    }
    private fun isPermissionGranted(context:Context):Boolean{
        return LocationService.hasLocationForegroundPermission(context)
    }
    private fun getCurrentGPS(): CurrentGPS {
        return locationService.provideLocation()

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionsResult: ")
        locationService.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
