package com.example.portfolio.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.portfolio.feature_weather.presentation.CurrentGPS
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

//https://stackoverflow.com/questions/32491960/android-check-permission-for-locationmanager

/*
    When first calling, even though permission is granted, they decided not to call getWeather data

 */
class LocationPermissionHandler(private val activity: Activity? = null, private val fragment: Fragment?= null, private val context:Context? = null): EasyPermissions.PermissionCallbacks {
    private val mContext = activity ?: (fragment!!.requireContext())
    private val mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    companion object {
        private const val TAG = "LocationService"
        fun hasLocationForegroundPermission(context:Context) =
            (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
    }

    operator fun invoke(){
        Log.d(TAG, "invoke: ")
        //requestPermission()
        //requestEnableGPS()
    }

    fun requestPermission(activity: Activity? = null, fragment: Fragment? =null){
        Log.d(TAG, "requestPermission: ")

        if(!hasLocationForegroundPermission(mContext))
            when{
                fragment != null ->{
                    Log.d(TAG, "requestPermission: fragment")
                    when{
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                            EasyPermissions.requestPermissions(
                                fragment,
                                "Please accept location permission. Otherwise, fragment app will not work properly",
                                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        }
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                            EasyPermissions.requestPermissions(
                                fragment,
                                "Without Location Permission, fragment app will not work properly",
                                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )

                            val shouldshow = ActivityCompat.shouldShowRequestPermissionRationale(
                                fragment.requireActivity(),
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                            Log.d(TAG, "requestPermission: $shouldshow")
                            //Q or higher require to request it separately and also device should work without the background location permission.
                            /*                        EasyPermissions.requestPermissions(
                                                        fragment,
                                                        "Newer phone requires background service permission" +
                                                                ", otherwise app will not work properly.",
                                                        constants.REQUEST_CODE_LOCATION_PERMISSION,
                                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                                    )*/
                        }
                        else ->{
                            EasyPermissions.requestPermissions(
                                fragment,
                                "Else is called..not cool",
                                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        }
                    }
                }
                activity != null ->{
                    Log.d(TAG, "requestPermission: activity")
                    when{
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                            EasyPermissions.requestPermissions(
                                activity,
                                "Please accept location permission. Otherwise, activity app will not work properly",
                                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        }
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                            EasyPermissions.requestPermissions(
                                activity,
                                "Without Location Permission, activity app will not work properly",
                                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )

                            val shouldshow = ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                            Log.d(TAG, "requestPermission: $shouldshow")
                            //Q or higher require to request it separately and also device should work without the background location permission.
                            EasyPermissions.requestPermissions(
                                activity,
                                "Newer phone requires background service permission" +
                                        ", otherwise app will not work properly.",
                                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            )
                        }
                        else ->{
                            EasyPermissions.requestPermissions(
                                activity,
                                "Else is called..not cool",
                                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        }
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsGranted: from LocationService.kt ")
    }
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsDenied: from LocationService.kt ")
        when{
            fragment != null ->{
                if(EasyPermissions.somePermissionPermanentlyDenied(fragment, perms)){
                    AppSettingsDialog.Builder(fragment).build().show()

                }else{
                    requestPermission()
                }
            }

            activity != null ->{
                if(EasyPermissions.somePermissionPermanentlyDenied(activity, perms)){
                    AppSettingsDialog.Builder(activity).build().show()

                }else{
                    requestPermission()
                }
            }
        }
    }

    private fun isGPSAvailable(locationManager:LocationManager = mLocationManager):Boolean{
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    fun requestEnableGPS(
        context:Context = mContext,
        onGranted: (CurrentGPS)->Unit = {},
        onDenied:(message:String) ->Unit = {},
    ){
        Timber.tag("LocationPermissionHandler").d("requestEnableGPS: is called")
        var currentGPS:CurrentGPS? = null

        if(!isGPSAvailable()){
            AlertDialog.Builder(context)
                .setMessage("Your GPS is disabled, please enable it to use the app properly")
                .setCancelable(false)
                .setPositiveButton("Okay",DialogInterface.OnClickListener{dialog, which ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(
                        context, intent, null
                    )
                    currentGPS = getCurrentGPS()
                    if(currentGPS != null) {
                        currentGPS?.let {
                            onGranted(it)
                        }
                    }
                    else
                        onDenied("GPS is enable but failed to get GPS")
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                    onDenied("User decided not to enable GPS")
                    dialog.cancel()
                })
                .create()
                .show()
        }else{ //GPS is enabled
            currentGPS = getCurrentGPS()
            if(currentGPS != null){
                currentGPS?.let{
                    onGranted(it)
                }
            }
            else
                onDenied("GPS is enable but failed to get GPS")
        }
    }

    @SuppressLint("MissingPermission", "ServiceCast")
    fun getCurrentGPS(): CurrentGPS? {
        Log.d(TAG, "getCurrentGPS: ")
/*        var scopeLocation:Location ? =location

        *//*        if(!isGPSAvailable() && hasLocationForegroundPermission(mContext)){
                    requestPermission()
                    requestEnableGPS()
                }*//*

        if(scopeLocation != null)
            return getGPS(scopeLocation)

        */

        //try one more time to get Location
        try{
            val scopeLocation = getLocation()

            val currentGPS = convertToCurrentGPS(scopeLocation)
            return currentGPS
        }
        catch (e:NullPointerException){
            Timber.tag("LocationPermissionHandler").d("getCurrentGPS: currentLocation is null and not able to find it")
            return null
        }
        catch(e:Exception){
            Timber.tag("LocationPermissionHandler").w("getCurrentGPS: Error Occured \n %s", e.message)
            return null
        }

        /*        Log.d(TAG, "provideLocation: is permission granted(): ${hasLocationForegroundPermission(mContext)}")
                var currentLocation:Location ? = null
                val currentGPS = CurrentGPS()
                if(!hasLocationForegroundPermission(mContext))
                    requestPermission()

                return if(location == null){
                    val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val providers = locationManager.allProviders

                    for(provider in providers){
                        //if currentLocation is not null then return
                        if(currentLocation?.latitude != null){
                            currentGPS.gps = mutableMapOf(
                                "latitude" to currentLocation.latitude.toInt(),
                                "longitude" to currentLocation.longitude.toInt())
                            return currentGPS
                        }

                        Log.d(TAG, "provideLocation: provider - $provider")
        *//*                locationManager.requestLocationUpdates(provider, 1000, 0F, object:android.location.LocationListener {
                    override fun onLocationChanged(location: Location) {
                        currentLocation=location
                    }
                })*//*

                locationManager.requestLocationUpdates(provider, 1000, 0F
                ) { mLocation:Location -> currentLocation = mLocation }

              *//*  currentLocation = locationManager.getLastKnownLocation(provider)
                    ?: locationManager.requestLocationUpdates(provider,0,0F,locationListener)*//*
            }
            //else return

            if(currentLocation != null){
                currentGPS.gps = mutableMapOf(
                    "latitude" to currentLocation!!.latitude.toInt(),
                    "longitude" to currentLocation!!.longitude.toInt())
                currentGPS
            }
            else {


            }
                currentGPS
        }else{
            currentGPS.gps = mutableMapOf(
                "latitude" to location.latitude.toInt(),
                "longitude" to location.longitude.toInt())
            return currentGPS
        }*/
    }

    private fun convertToCurrentGPS(location:Location?):CurrentGPS?{
        if(location == null) return null
        return CurrentGPS(mutableMapOf(
            "latitude" to location.latitude.toInt(),
            "longitude" to location.longitude.toInt()
        ))
    }

    @SuppressLint("MissingPermission")
    private fun getLocation():Location?{
        Log.d(TAG, "getLocation: ")
        var curLocation:Location? = null

        val locationListener = object:LocationListener {
            override fun onLocationChanged(location: Location) {
                Timber.tag("LocationPermissionHandler").d("onLocationChanged: ")
                //getLocation(location)
                curLocation = location

            }
            override fun onLocationChanged(locations: MutableList<Location>) {
                super.onLocationChanged(locations)
            }

            override fun onFlushComplete(requestCode: Int) {
                super.onFlushComplete(requestCode)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
            }
        }

        //if locationListener updated location, then return it
        curLocation?.let{ return it }

        //otherwise, find lastknownlocation and return it.
        val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.allProviders

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F,locationListener)
        for(provider in providers){
            locationManager.requestLocationUpdates(provider, 300000, 0F,locationListener)
            Log.d(TAG, "provideLocation: provider - $provider")
            /*if(curLocation != null)  {
                locationManager.removeUpdates(locationListener)

            }*/
            curLocation = locationManager.getLastKnownLocation(provider)
        }

        if(curLocation == null) Timber.tag("LocationPermissionHandler").d("getLocation: failed to find location")
        return curLocation
/*        return if(location == null){
            val fusedLocationProviderClient: FusedLocationProviderClient
            = FusedLocationProviderClient()

        }*/

    }




}
