package com.example.portfolio.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

//https://stackoverflow.com/questions/32491960/android-check-permission-for-locationmanager
class LocationService(private val activity: Activity? = null, private val fragment: Fragment?= null):LocationListener, EasyPermissions.PermissionCallbacks {
    private val mContext = activity ?: (fragment!!.requireContext())
    private var mLocationManager: LocationManager? = null
    private var mLocation: Location? = null
    private val cts = CancellationTokenSource()

    var longitude:Int = 0
    var latitude:Int? = 0

    operator fun invoke(){
        initLocationService()
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
    //With Easypermission
    private fun requestPermission(){

        if(isPermissionAvailable())
            return

        if(activity == null) {
            when{
                Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                    EasyPermissions.requestPermissions(
                        fragment!!,
                        "Please accept location permission. Otherwise, this app will not work properly",
                        constants.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    EasyPermissions.requestPermissions(
                        fragment!!,
                        "Without Location Permission, this app will not work properly",
                        constants.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                }
                else ->{
                    EasyPermissions.requestPermissions(
                        fragment!!,
                        "Without Location Permission, this app will not work properly",
                        constants.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )

                }
            }
        }
        else{
            when{
                Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                    EasyPermissions.requestPermissions(
                        activity,
                        "Please accept location permission. Otherwise, this app will not work properly",
                        constants.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    EasyPermissions.requestPermissions(
                        activity,
                        "Without Location Permission, this app will not work properly",
                        constants.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                }
                else ->{
                    EasyPermissions.requestPermissions(
                        activity,
                        "Without Location Permission, this app will not work properly",
                        constants.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )

                }
            }
        }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        when{
            fragment == null -> {
                //when user is permanently denied permission
                if(EasyPermissions.somePermissionPermanentlyDenied(activity!!, perms)){
                    AppSettingsDialog.Builder(activity).build().show()

                }else{ //not permanently denied but first deny, then request permission again.
                    requestPermission()
                }
            }
            activity == null ->{
                //when user is permanently denied permission
                if(EasyPermissions.somePermissionPermanentlyDenied(fragment, perms)){
                    AppSettingsDialog.Builder(fragment).build().show()

                }else{ //not permanently denied but first deny, then request permission again.
                    requestPermission()
                }
            }
        }
    }

    private fun initLocationService(): Boolean{
       if(!isPermissionAvailable()){
           Log.d(TAG, "initLocationService: permission is not granted")
            //Permission is not granted
            //request permission
           requestPermission()
           return false
        }
        //else Permission is granted
        Log.d(TAG, "initLocationService: permission is granted")

        var gpsEnabled = false
        var networkEnabled = false

        initLM()

        mLocation = updateLocation()

        try{
            gpsEnabled = mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch(e:Exception){

        }
        try{
            networkEnabled = mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }catch(e:Exception){

        }
        if(!gpsEnabled && !networkEnabled){
            AlertDialog.Builder(mContext)
                .setMessage("Please turn on GPS or Network for this service")
                .setPositiveButton("Enable", object: OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        mContext.startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    }
                })
                .setNegativeButton("Cancel", null)
                .show()
        }
        return true
    }

    @SuppressLint("MissingPermission", "VisibleForTests")
    private fun updateLocation(location:Location? = null):Location{
        Log.d(TAG, "updateLocation: isLMAvailable() = ${isLMAvailable()}")

        if(location != null){
            mLocation = location
            return mLocation as Location
        }
        //mCurlocation = (location ?: mLocationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)) as Location

        val providers = mLocationManager!!.allProviders
        val bestLocation:Location? = null
        for(provider in providers){
            mLocation = mLocationManager!!.getLastKnownLocation(provider)!!
            if(mLocation == null)
                continue
            if(bestLocation != null || mLocation?.accuracy!! > bestLocation?.accuracy!!){
                // Found best last known location: %s", l);
                mLocation = bestLocation
            }
        }

        //https://developer.android.com/training/location/request-updates
        //https://developers.google.com/android/reference/com/google/android/gms/tasks/CancellationToken
        if(mLocation?.latitude == null) {
            val locationRequest = LocationRequest.create().apply{
                interval = 100000
                fastestInterval = 30000
                priority = PRIORITY_HIGH_ACCURACY//LocationRequest.PRIORITY_HIGH_ACCURACY
                maxWaitTime = 100
            }
            val locationCallback = object:LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    super.onLocationAvailability(locationAvailability)
                }
            }

/*            val task = FusedLocationProviderClient(mContext)
                .requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
                .result*/
            mLocation = FusedLocationProviderClient(mContext)
                .getCurrentLocation(PRIORITY_HIGH_ACCURACY,cts.token)
                .result

        }

        Log.d(TAG, "updateLocation: is currentLocation null? ${mLocation?.provider} ${mLocation?.latitude}, ${mLocation?.longitude}")
        return mLocation!!
    }

    fun onStop(){
        cts.cancel()

    }
    override fun onLocationChanged(location: Location) {
        updateLocation(location)
    }

    private fun isLMAvailable():Boolean{
        return mLocationManager != null
    }
    private fun initLM(){
        if(mLocationManager == null){
            Log.d(TAG, "initLM: locationManager is null and now initiated")
            mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            //if location manager is still null
            if(mLocationManager == null){
                Log.d(TAG, "initLM: location manager is still ${isLMAvailable()}")
            }
        }
    }

    fun getCoordination():Map<String, Int?>{
        //Double Check although it is invoked at the beginning
        if(!isPermissionAvailable()){
            Log.d(TAG, "getCoordination: permission is not available")
            requestPermission()}
        if(!isLMAvailable()){
            Log.d(TAG, "getCoordination: location manager is not available calling initLM()s")
            initLM()
        }

        try{
            val location = updateLocation()
            longitude = location.longitude.toInt()
            latitude = location.latitude.toInt()
        }catch (e:Exception){
            Log.d(TAG, "getCoordination: Error while return coordination ${e.message}")
        }

        return mapOf("latitude" to latitude, "longitude" to longitude)
    }

    companion object {
        private const val TAG = "LocationService"
    }
}

/*

public class LocationService implements LocationListener  {
 //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    //The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;

    private static LocationService instance = null;

    private LocationManager locationManager;
    public Location location;
    public double longitude;
    public double latitude;


    /**
     * Singleton implementation
     * @return
     */
    public static LocationService getLocationManager(Context context)     {
        if (instance == null) {
            instance = new LocationService(context);
        }
        return instance;
    }

    /**
     * Local constructor
     */
    private LocationService( Context context )     {

        initLocationService(context);
        LogService.log("LocationService created");
    }



    /**
     * Sets up location service after permissions is granted
     */
    @TargetApi(23)
    private void initLocationService(Context context) {


        if ( Build.VERSION.SDK_INT >= 23 &&
             ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
             ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        try   {
            this.longitude = 0.0;
            this.latitude = 0.0;
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (forceNetwork) isGPSEnabled = false;

            if (!isNetworkEnabled && !isGPSEnabled)    {
                // cannot get location
                this.locationServiceAvailable = false;
            }
            //else
            {
                this.locationServiceAvailable = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null)   {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateCoordinates();
                    }
                }//end if

                if (isGPSEnabled)  {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null)  {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        updateCoordinates();
                    }
                }
            }
        } catch (Exception ex)  {
            LogService.log( "Error creating location service: " + ex.getMessage() );

        }
    }


    @Override
    public void onLocationChanged(Location location)     {
        // do stuff here with location object
    }
}

 */

