package com.example.portfolio.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import pub.devrel.easypermissions.EasyPermissions

fun main(){
    println("Hello World")
}

object PermissionHandler:EasyPermissions.PermissionCallbacks {
    private const val TAG = "PermissionHandler"

    fun isPermissionAvailable(perm:String, context: Context):Boolean{
        return EasyPermissions.hasPermissions(
            context,
            perm
        )
    }

    fun requestPermission(
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
                    if(!isPermissionAvailable(it,context)){
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
                    if(!isPermissionAvailable(it,context)){
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
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }


}
sealed interface NetworkStatus{
    object Available:NetworkStatus
    object Unavailable:NetworkStatus
    object ConnectionLost:NetworkStatus
}



