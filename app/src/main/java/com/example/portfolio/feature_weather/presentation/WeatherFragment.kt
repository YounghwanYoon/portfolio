package com.example.portfolio.feature_weather.presentation



import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portfolio.R
import com.example.portfolio.databinding.FragmentWeatherBinding
import com.example.portfolio.feature_weather.domain.model.forecasthourly.Period
import com.example.portfolio.feature_weather.presentation.adapter.CustomData
import com.example.portfolio.feature_weather.presentation.adapter.WeatherHourlyAdapter
import com.example.portfolio.feature_weather.presentation.adapter.WeatherWeeklyAdapter
import com.example.portfolio.feature_weather.presentation.helper.WeatherHelper
import com.example.portfolio.utils.DataState
import com.example.portfolio.utils.LocationPermissionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
class WeatherFragment : Fragment(R.layout.fragment_weather){
    private lateinit var locationHandler:LocationPermissionHandler
    private val vm: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentWeatherBinding
    private lateinit var weeklyAdapter: WeatherWeeklyAdapter
    private lateinit var hourlyAdapter: WeatherHourlyAdapter
    private lateinit var navController: NavController

    companion object {
        private const val TAG = "WeatherFragment"
    }

    //lifecycle relate
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: ")
        navController = findNavController()
        binding = FragmentWeatherBinding.inflate(inflater,container, false)
        val view = binding.root
        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
/*        locationHelper()
        subscribeVM()*/
        super.onStart()
    }

    private fun subscribePermissionState(){
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.CREATED){
                vm.permState.collect{
                    when(it){
                        // * Requesting state will be called at every time app is launched/or at Created State
                        // * Then it will check permission state, if it not granted, request permission from User
                        is PermissionState.Requesting -> {
                            Timber.tag("WeatherFragment").d("subscribePermissionState: Requesting")
                            if(!LocationPermissionHandler.hasLocationForegroundPermission(this@WeatherFragment.requireContext())){
                                Timber.tag("WeatherFragment").d("subscribePermissionState: permission has not granted.")
                                initLocationHandler(this@WeatherFragment)
                            }else{
                                Timber.tag("WeatherFragment").d("subscribePermissionState: permission is granted")
                                vm.setPermState(PermissionState.Granted)
                            }
                        }
                        is PermissionState.Error -> {
                            Timber.tag(TAG).e("Permission Error Occured")
                        }

                        // * When Permission is Granted, request user to enable GPS, if it is not.
                        is PermissionState.Granted -> {
/*                            Timber.tag("WeatherFragment").d("subscribePermissionState: Permission is granted. Requesting Enable GPS")
                            locationHandler.requestEnableGPS(
                                context = this@WeatherFragment.requireContext(),
                                onGranted = {
                                    Timber.tag("WeatherFragment").d("subscribePermissionState: GPS Enable is Granted")
                                    vm.setGPSState(
                                        GPSState.Granted(it)
                                    )
                                },
                                onDenied = {
                                    Timber.tag("WeatherFragment").d("subscribePermissionState: Denied because $it")
                                }
                            )*/
                        }

                        is PermissionState.Denied -> {
                            vm.setPermState(PermissionState.Denied)
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                                activity?.onBackInvokedDispatcher
                                ///MainFragment
                                //navController.navigate()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Check Permission Status
        vm.setPermState(PermissionState.Requesting)
        subscribePermissionState()



        Log.d(TAG, "onViewCreated: ")
        //setWeather(requireContext())
        initRecyclerview()
        setUpForecastRecyclerView()
        setUpHourlyRecycleView()
        subscribeVM()
        super.onViewCreated(view, savedInstanceState)
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
            //remove divider between recyclerview item
            this.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.HORIZONTAL))
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

            //remove divider between recyclerview item
            this.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.HORIZONTAL))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpView(period:Period){
        binding.apply{
            weatherFragmentLinear.setBackgroundResource(WeatherHelper.selectedSeasonalImage())
            period.shortForecast?.let{
                currentHourImage.setImageResource( WeatherHelper.selectWeatherImage(it))
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
    @SuppressLint("NotifyDataSetChanged" )
    private fun subscribeVM(){
        //must be separate lifecycleScope or data will not be updated.
        //but why?

        //TODO Adopt GPSState, then GPS is granted, call vm to update data.

        viewLifecycleOwner.lifecycleScope.launch{
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

        //Subscribe VM for hourly forecast data
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
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

        }
        //Subscribe VM for weekly forecast data
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
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
    }


    private fun initLocationHandler(fragment: Fragment){
        Timber.tag("WeatherFragment").d("initLocationHandler: is called")
        locationHandler = LocationPermissionHandler(fragment = fragment)
        locationHandler.requestPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionsResult: ")
        locationHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val isGranted = LocationPermissionHandler.hasLocationForegroundPermission(requireContext())
        if(!isGranted){
            Log.d(TAG, "onRequestPermissionsResult: is Granted false - $isGranted ")
            vm.setPermState(PermissionState.Denied)
        }
        else{
            Log.d(TAG, "onRequestPermissionsResult: is Granted $isGranted ")
            vm.setPermState(PermissionState.Granted)
            //this.onStart()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}
