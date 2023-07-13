package com.example.portfolio.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.google.accompanist.permissions.PermissionStatus
import pub.devrel.easypermissions.EasyPermissions


object PermissionHandler:EasyPermissions.PermissionCallbacks {
    private const val TAG = "PermissionHandler"
    private val listOfPermissions = arrayOf<String>(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    private const val permRequestCode = 123

    private var _permState:PermsStatus = PermsStatus.Requesting

    operator fun invoke(vararg perms:String= emptyArray(), context:Context, activity: Activity):PermsStatus{

        val isPermGranted =
            if(perms.isNotEmpty())
                arePermissionAvailable(*perms, context = context)
            else
                arePermissionAvailable(*listOfPermissions, context = context)


        return when(isPermGranted){
            true ->{
                PermsStatus.Granted
            }

            false ->{
                requestPermission(
                    context =context,
                    rationale = "Location Permission is required to obtain weather information for a specific location",
                    code = permRequestCode,
                    activity = activity,
                    fragment = null
                )
                PermsStatus.Requesting
            }
        }

    }

    fun arePermissionAvailable(vararg perms:String, context: Context):Boolean{
        return EasyPermissions.hasPermissions(context, *perms)
    }

    fun isPermissionAvailable(perm:String, context: Context):Boolean{
        return EasyPermissions.hasPermissions(context, perm)
    }

    private fun requestPermission(
            vararg perms:String,
            context:Context,
            rationale:String,
            code:Int,
            activity: Activity?= null,
            fragment: Fragment?=null
    ){
        when{
            activity != null -> {
                perms.forEach{
                    if(!isPermissionAvailable(it,context = context)){
                        EasyPermissions.requestPermissions(
                            activity,
                            rationale,
                            code,
                            it
                        )
                    }
                }
            }
            fragment != null ->{
                perms.forEach{
                    if(!isPermissionAvailable(it, context =context)){
                        EasyPermissions.requestPermissions(
                            fragment,
                            rationale,
                            code,
                            it
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
        when{
            requestCode == permRequestCode ->{
                _permState = PermsStatus.Granted
            }
            else ->{
                _permState = PermsStatus.Denied
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        _permState = PermsStatus.Denied
    }


}
sealed class GPSStatus: PermsStatus {
    object Available:GPSStatus()
    object Unavailable:GPSStatus()
    object Error:GPSStatus()

}

sealed interface PermsStatus{
    object Granted:PermsStatus
    object Denied:PermsStatus
    object Requesting:PermsStatus

}



