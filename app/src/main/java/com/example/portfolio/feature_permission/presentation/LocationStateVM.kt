package com.example.portfolio.feature_permission.presentation

import android.Manifest
import android.os.Build
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class LocationStateVM:PermissionTracker, GPSTracker, ViewModel() {
    
    private val _permState = MutableStateFlow<PermissionState>(PermissionState.Requesting)
    val permState = _permState.asStateFlow()

    private val _gpsState = MutableStateFlow<GPSState>(GPSState.Requesting)
    val gpsState = _gpsState.asStateFlow()

    companion object{
        val PERMISSION_CODE = 123
        val PERMISSIONS = arrayListOf<String>(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    init{
        updatePermissionByVersionCode()
    }
    private fun updatePermissionByVersionCode(){
        when{
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ->{
                PERMISSIONS.add(Manifest.permission.FOREGROUND_SERVICE)
            }
        }
    }

    override fun permissionResult(result: Boolean) {
        when(result){
            true ->{
                setPermState(PermissionState.Granted)
            }
            false ->{
                setPermState(PermissionState.Denied("User Denied Permission"))
            }
        }
    }

    fun setPermState(updatedState:PermissionState){
        when(updatedState){
            is PermissionState.Denied -> {
                Timber.tag("PermissionStateVM").d("setPermState: Denied")
                Timber.tag("PermissionStateVM").d("setPermState: Reason : ${updatedState.message}")

                _permState.update {
                    updatedState
                }
            }
            PermissionState.Granted -> {
                Timber.tag("PermissionStateVM").d("setPermState: Granted")

                _permState.update {
                    updatedState
                }
            }
            PermissionState.Requesting -> {
                Timber.tag("PermissionStateVM").d("setPermState: Requesting")
            }
        }
    }

    fun setGPSState(updatedState:GPSState){
        when(updatedState){
            is GPSState.NotReceived -> {
                Timber.tag("PermissionStateVM").d("setGPSState: Not Received \n${updatedState.message}")
                
                if(permState.value == PermissionState.Granted){
                    Timber.tag("PermissionStateVM").d("setGPSState: GPS is not turned on")

                }
                
            }
            is GPSState.Received -> {
                Timber.tag("PermissionStateVM").d("setGPSState: Received")
                _gpsState.update{
                    updatedState
                }
            }
            GPSState.Requesting -> {
                Timber.tag("PermissionStateVM").d("setGPSState: Requesting")
            }
        }
    }

    sealed interface PermissionState{
        object Granted: PermissionState
        data class Denied(val message:String): PermissionState
        object Requesting: PermissionState
    }

    sealed interface GPSState{
        data class Received(val locationInfo: LocationInfo): GPSState
        data class NotReceived(val message:String): GPSState
        object Requesting: GPSState

    }

    override fun onLocationReceived(lattitude: Double, longitude: Double) {
        setGPSState(
            GPSState.Received(LocationInfo(lattitude,longitude))
        )
        Timber.tag("PermissionStateVM").d("onGPSChanged: lat $lattitude & long $longitude")
    }

    override fun onFailedToReceivedLocation() {
        setGPSState(
            GPSState.NotReceived("GPS info not able to retrieve. Maybe user need to turn on GPS signal")
        )
    }
}

data class LocationInfo(
    var lattitude: Double,
    var longitude: Double
)

interface PermissionTracker{
    fun permissionResult(result:Boolean)
}

interface GPSTracker{
    fun onLocationReceived(lattitude:Double, longitude:Double)
    fun onFailedToReceivedLocation()
}