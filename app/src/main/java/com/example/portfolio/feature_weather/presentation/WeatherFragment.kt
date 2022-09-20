package com.example.portfolio.feature_weather.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portfolio.R
import com.example.portfolio.databinding.FragmentWeatherBinding
import com.example.portfolio.feature_myapp.presentation.viewmodel.*
import com.example.portfolio.feature_weather.domain.model.Weather
import com.example.portfolio.feature_weather.domain.model.forecast.Forecast
import com.example.portfolio.feature_weather.presentation.adapter.WeatherWeeklyAdapter
import com.example.portfolio.utils.DataState
import com.example.portfolio.utils.LocationService
import com.example.portfolio.utils.TrackingUtility
import com.example.portfolio.utils.constants.REQUEST_CODE_LOCATION_PERMISSION
import kotlinx.android.synthetic.main.fragment_weather.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class WeatherFragment : Fragment(R.layout.fragment_weather)/*, EasyPermissions.PermissionCallbacks, LocationListener*/ {

    private lateinit var binding: FragmentWeatherBinding
    private val vm: WeatherViewModel by activityViewModels()
    private val weeklyAdapter = WeatherWeeklyAdapter()
    private lateinit var locationService:LocationService

    companion object {
        private const val TAG = "WeatherFragment"
    }

    private fun setUpRecyclerView(){
        binding.weeklyforecastRecyclerview.apply{
            this.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = weeklyAdapter
        }
    }
    private fun toggleProgress(){
        binding.progressBar.apply{
            when(this.visibility){
                View.VISIBLE -> this.visibility = View.GONE
                View.GONE -> this.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun handlePermission(){
        val locationJob = CoroutineScope(Dispatchers.IO).async {
            locationService = LocationService(fragment = this@WeatherFragment)
        }
        viewLifecycleOwner.lifecycleScope.launch{
            locationJob.await()
            setGPSState()
        }



    }
    private fun setGPSState() {
        val coordination = locationService.getCoordination()
        Log.d(TAG, "setGPSState: coordination is ${coordination["longitude"]}")
        
        vm.setPermState(
            GPSState(
                isGPSGranted = true,
                currentGPS = CurrentGPS(coordination),
                currentTime = Calendar.getInstance().timeInMillis
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handlePermission()
        //locationService = LocationService(fragment = this)

        setUpRecyclerView()
        subscribeObserver()
        //initLocationService()

        super.onViewCreated(view, savedInstanceState)
    }



    private fun subscribeObserver(){
/*
       viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                vm.permState.collect{ gpsState ->
                    Log.d(TAG, "subscribeObserver: GPS State ${gpsState.currentGPS?.gps?.get("longitude")}")
                    if(gpsState.isGPSGranted && gpsState.currentGPS !=null)
                        vm.getForecasts(gpsState.currentGPS.gps!!)

                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.dataState.collect{ dataState ->
                    when(dataState){
                        is DataState.Error -> {
                            Log.d(TAG, "subscribeObserver: something went wrong")
                            toggleProgress()
                            Toast.makeText(requireContext(), "Something went wrong, collecting data is failed.", Toast.LENGTH_SHORT).show()
                        }
                        DataState.Loading -> {
                            Log.d(TAG, "subscribeObserver: loading~")
                            toggleProgress()
                        }
                        is DataState.SuccessSingleData -> {
                            Log.d(TAG, "subscribeObserver: Success")
                            toggleProgress()
                            updateWeeklyData(dataState.data)
                        }
                    }
                }
            }
        }      */

       viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                vm.permState.collect{ gpsState ->
                    Log.d(TAG, "subscribeObserver: GPS State ${gpsState.currentGPS?.gps?.get("longitude")}")
                    if(gpsState.isGPSGranted && gpsState.currentGPS !=null)
                        vm.getWeather(gpsState.currentGPS)

                }
            }
        }
/*
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.dataState.collect{ dataState ->
                    when(dataState){
                        is DataState.Error -> {
                            Log.d(TAG, "subscribeObserver: something went wrong")
                            toggleProgress()
                            Toast.makeText(requireContext(), "Something went wrong, collecting data is failed.", Toast.LENGTH_SHORT).show()
                        }
                        is DataState.Loading<Weather> -> {
                            Log.d(TAG, "subscribeObserver: loading~")
                            toggleProgress()
                        }
                        is DataState.Success -> {
                            Log.d(TAG, "subscribeObserver: Success")
                            toggleProgress()
                            //updateWeeklyData(dataState.data)
                        }
                    }
                }
            }
        }
*/
    }

    private fun updateWeeklyData(data:Forecast?){
        data?.let{
            weeklyAdapter.updateData(it)
        }
    }


    /*
    @SuppressLint("MissingPermission")
    //https://stackoverflow.com/questions/2227292/how-to-get-latitude-and-longitude-of-the-mobile-device-in-android
    private fun getCoordination():Map<String,Int?>{
        Log.d(TAG, "getLocation: getLocation() is called")
        val longitude: Int?
        val latitude:Int?
        //Log.d(TAG, "getCoordination: is GPS or Network Enabled? ${isGPSOrNetworkEnabled()}")
        Log.d(TAG, "getCoordination: Is Permission Available()? ${isPermissionAvailable()}")
//isGPSOrNetworkEnabled() &&
        if( isPermissionAvailable()){
            Log.d(TAG, "getLocation: is ready to get location")
            if(manager == null)
                manager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            //Enableing both GPS_Providers and NETWORK_PROVIDER due to different ability based on the devices
            manager!!.getProviders(true)

            currentLocation = updateLocation()

            if(currentLocation == null){
                manager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,100F,this)
                manager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,100F,this)
            }

            Log.d(TAG, "getCoordination: is Location null? ${currentLocation}")

            if(currentLocation !=null){

                longitude = currentLocation!!.longitude.toInt()
                latitude = currentLocation!!.latitude.toInt()
                gpsData = mapOf("latitude" to latitude, "longitude" to longitude)
            }

            vm.setPermState(GPSState(
                isGPSGranted = true,
                currentGPS = CurrentGPS(gpsData),
                currentTime = Calendar.getInstance().time.time
            ))

        }else{
            Log.d(TAG, "getLocation: There is a problem with permission")
        }

        return gpsData
    }



    @SuppressLint("MissingPermission")
    private fun updateLocation(location:Location? = null):Location?{
        return location ?: manager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    }
    override fun onLocationChanged(location: Location) {
        updateLocation(location)
    }

    private fun isPermissionAvailable():Boolean{

        when{
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                return (
                        ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED
                    )
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                return (
                        ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED
                    )
            }
            else ->{
                return (
                        ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED
                        )
            }
        }
    }

    private fun isGPSOrNetworkEnabled():Boolean{
        var gps_enabled = false
        var network_enabled = false

        try{
            gps_enabled = manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch(e:Exception){

        }
        try{
            network_enabled = manager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }catch(e:Exception){

        }
        if(!gps_enabled && !network_enabled){
            AlertDialog.Builder(requireContext())
                .setMessage("Please turn on GPS or Network for this service")
                .setPositiveButton("Enable", object: DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        requireContext().startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    }
                })
                .setNegativeButton("Cancel", null)
                .show()
        }


        return !(!gps_enabled && !network_enabled)

    }

    //FrameWork that is called whenever we request permission
    //update the result to EasyPermission.
    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //Google Recommended way
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionsResult: Success")
                getCoordination()
            }
                //checkAndRequestLocation()
            else {
                Log.d(TAG, "onRequestPermissionsResult: Failed")
                AppSettingsDialog.Builder(this)
                    .setRationale("Without your permissions, this app will not work properly.")
                    .build()
                    .show()
            }
        }
        else{
            Log.d(TAG, "onRequestPermissionsResult: Something else is granted")
            AppSettingsDialog.Builder(this)
                .setRationale("Something else is granted...")
                .build()
                .show()
        }

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    //With Easypermission
    private fun requestPermission(){
        Log.d(TAG, "requestPermission: requestPermission is called")
        if(TrackingUtility.hasLocationPermission(requireContext())){
            return
        }
        //check whether user deny or check
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                this,
                "Please accept location permission. Otherwise, this app will not work properly",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){ //Version_CODE.Q requires background location
            Log.d(TAG, "requestPermission: current SDK Version is ${Build.VERSION.SDK_INT} which is higher or equal to Q")
            EasyPermissions.requestPermissions(
                this,
                "Without Location Permission, this app will not work properly",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else{ // Higher than Version Q (29)
            EasyPermissions.requestPermissions(
                this,
                "Please accept location permission. Otherwise, this app will not work properly",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        }
    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(
            TAG, "onPermissionsGranted: Request Code is $requestCode and \n " +
                "Permission is granted ${TrackingUtility.hasLocationPermission(requireContext())}")

        setPermState(GPSState(
            isGPSGranted = true,
            currentGPS = CurrentGPS(getCoordination()),
            currentTime = 0
        ))

    }
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //when user is permanently denied permission
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            setPermState(GPSState())
            //This lead user to app setting where he/she can allow the denied permission.
            AppSettingsDialog.Builder(this).build().show()
        }else{ //not permanently denied but first deny, then request permission again.
            requestPermission()
        }
    }
    private fun setPermState(state: GPSState){
        vm.setPermState(state)

/*        when(state){
            is PermissionState.Error -> {
                Log.d(TAG, "setPermState: Error Requesting GPS")
            }

            is PermissionState.Requesting -> {
                Log.d(TAG, "setPermState: Requesting GPS")
            }

            is PermissionState.Success -> {
                Log.d(TAG, "setPermState: Received GPS Data")
                //getTestAPI()
                vm.getForecasts(state.data)
                viewLifecycleOwner.lifecycleScope.launch{

                    vm.dataState.collect{ dataState ->
                        when(dataState){
                            is DataState.Error -> {
                                Log.d(TAG, "setPermState: Error - there is an error when retrieving data from server")
                            }
                            DataState.Loading -> Log.d(TAG, "setPermState: loading data")
                            is DataState.Success -> Log.d(TAG, "setPermState: this is for multiple data but not in use")
                            is DataState.SuccessSingle -> {
                                Log.d(TAG, "setPermState: received weather data from server/api \n" +
                                        "data is ${dataState.data}")
                            }
                        }
                    }
                }
            }
        }*/

    }


     */
}
