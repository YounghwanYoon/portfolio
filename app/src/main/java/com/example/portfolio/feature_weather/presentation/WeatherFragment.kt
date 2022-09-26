package com.example.portfolio.feature_weather.presentation



import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.portfolio.R
import com.example.portfolio.feature_myapp.presentation.viewmodel.*
import com.example.portfolio.utils.LocationService


class WeatherFragment : Fragment(R.layout.fragment_weather){
    private lateinit var locationService:LocationService
    
    companion object {
        private const val TAG = "WeatherFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        locationHandler()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun locationHandler(){
        locationService = LocationService(requireActivity())
        var currentGPS:CurrentGPS? = null
        if(LocationService.hasLocationForegroundPermission(requireContext()))
            currentGPS = locationService.provideLocation()

        currentGPS?.let{
            Log.d(TAG, "locationHandler: ${it.gps?.get("longitude")}")
        }
    }

    override fun onResume() {

        super.onResume()
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

/*
//GPS was fixed version
class WeatherFragment : Fragment(R.layout.fragment_weather), EasyPermissions.PermissionCallbacks,
    LocationListener {
    private lateinit var mContext:Context
    companion object {
        private const val TAG = "WeatherFragment"

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mContext =requireContext()
        requestPermission()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun isPermissionAvailable():Boolean{
        when{
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                return (
                        ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED
                        )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                return (
                        ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED
                        )
            }
            else ->{
                return (
                        ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED
                        )
            }
        }
    }

    private fun hasLocationPermission():Boolean{
        when{
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
               return EasyPermissions.hasPermissions(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                return EasyPermissions.hasPermissions(
                    mContext,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            else ->
                    return EasyPermissions.hasPermissions(
                        mContext,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
        }
    }


    private fun requestPermission(){
        when{
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                EasyPermissions.requestPermissions(
                    this,
                    "Please accept location permission. Otherwise, this app will not work properly",
                    constants.REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                EasyPermissions.requestPermissions(
                    this,
                    "Without Location Permission, this app will not work properly",
                    constants.REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                val shouldshow = ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                Log.d(TAG, "requestPermission: $shouldshow")
                //Q or higher require to request it separately and also device should work without the background location permission.
                EasyPermissions.requestPermissions(
                    this,
                    "Newer phone requires background service permission" +
                            ", otherwise app will not work properly.",
                    constants.REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                )
            }
            else ->{
                EasyPermissions.requestPermissions(
                    this,
                    "Else is called..not cool",
                    constants.REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsGranted: ")
    }
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsDenied: ")
        Toast.makeText(mContext, "Perm Denied", Toast.LENGTH_SHORT).show()
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()

        }else{
            requestPermission()
        }
    }
    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged: ")
        provideLocation(location)
    }
    private fun hasLocationForegroundPermission() =
        ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission", "ServiceCast")
    private fun provideLocation (location:Location?=null):CurrentGPS{
        Log.d(TAG, "provideLocation: is permission granted(): ${hasLocationForegroundPermission()}")
        var currentLocation:Location ? = null
        val currentGPS = CurrentGPS()

        return if(location == null){
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = locationManager.allProviders

            for(provider in providers){
                //if currentLocation is not null then return
                if(currentLocation?.latitude != null){
                    currentGPS.gps = mutableMapOf(
                        "latitude" to currentLocation!!.latitude.toInt(),
                        "longitude" to currentLocation!!.longitude.toInt())
                    return currentGPS
                }

                Log.d(TAG, "provideLocation: $provider")
                locationManager.requestLocationUpdates(provider, 1000, 0F, object:android.location.LocationListener {
                    override fun onLocationChanged(location: Location) {
                        currentLocation=location
                    }
                })
                currentLocation = locationManager.getLastKnownLocation(provider)
            }
            //else return
            currentGPS.gps = mutableMapOf(
                "latitude" to currentLocation!!.latitude.toInt(),
                "longitude" to currentLocation!!.longitude.toInt())
            currentGPS
        }else{
            currentGPS.gps = mutableMapOf(
                "latitude" to location!!.latitude.toInt(),
                "longitude" to location!!.longitude.toInt())
            return currentGPS
        }
    }

    private fun testGettingGPS(){
        Log.d(TAG, "testGettingGPS: provideLocation")
    }
}

*/












/*
    private lateinit var binding: FragmentWeatherBinding
    private val vm: WeatherViewModel by activityViewModels()
    private val weeklyAdapter = WeatherWeeklyAdapter()
    private lateinit var locationService:LocationService
    private lateinit var manager : LocationManager

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

    //handle permission
    private fun handlePermission(){
        locationService = LocationService(fragment = this@WeatherFragment)
    }
/*    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionsResult:send")
        locationService.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }*/

    //request location
    private fun requestLocation():Map<String,Int?>{
        return mutableMapOf("latitude" to 37, "longitude" to -122)
        //return locationService.getCoordination()
    }
    private fun setGPSState() {
        val currentGPS = CurrentGPS(requestLocation())
        Log.d(TAG, "setGPSState: coordination is ${currentGPS.gps?.get("longitude")}")
        vm.setPermState(PermissionState.Granted(currentGPS))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handlePermission()
        //locationService = LocationService(fragment = this)
        setGPSState()
        setUpRecyclerView()
        //subscribeObserver()
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

                launch{
                    vm.forecastState.collect{ forecastState ->
                        when(forecastState){
                            is DataState.Error -> Log.d(TAG, "subscribeObserver: Error")
                            is DataState.Loading -> Log.d(TAG, "subscribeObserver: Loading")
                            is DataState.Success -> Log.d(TAG, "subscribeObserver: Success")
                        }
                    }
                }

                launch{
                    vm.forecastHourlyState.collect{ forecastHourlyState ->
                        when(forecastHourlyState){
                            is DataState.Error -> Log.d(TAG, "subscribeObserver: Error")
                            is DataState.Loading -> Log.d(TAG, "subscribeObserver: Loading")
                            is DataState.Success -> Log.d(TAG, "subscribeObserver: Success")
                        }
                    }
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

    override fun onStop() {
        locationService.onStop()
        super.onStop()
    }


    @SuppressLint("MissingPermission")
    //https://stackoverflow.com/questions/2227292/how-to-get-latitude-and-longitude-of-the-mobile-device-in-android
    /*private fun getCoordination():Map<String,Int?>{
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
    }*/



    private fun updateLocation(location: Location? = null): Location?{
        return location ?: manager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    }
    override fun onLocationChanged(location: Location) {
        updateLocation(location)
    }

    private fun isPermissionAvailable():Boolean{

        when{
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                return (
                        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
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

/*
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
*/

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
                //getCoordination()
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

/*        setPermState(GPSState(
            isGPSGranted = true,
            currentGPS = CurrentGPS(getCoordination()),
            currentTime = 0
        ))*/

    }
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //when user is permanently denied permission
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
/*
            setPermState(GPSState())
*/
            //This lead user to app setting where he/she can allow the denied permission.
            AppSettingsDialog.Builder(this).build().show()
        }else{ //not permanently denied but first deny, then request permission again.
            requestPermission()
        }
    }
/*
    private fun setPermState(state: GPSState){
        vm.setPermState(state)

*/
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
        }*//*


    }
*/

/*

}


 */