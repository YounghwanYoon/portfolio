package com.example.portfolio.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


@SuppressLint("ObsoleteSdkInt")
object NetworkStateTracker{
    private const val TAG = "PermissionHandler"
    private val NetworkStateTrackerImpl:NetworkStateTrackerBase

    init{
        NetworkStateTrackerImpl = when{
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> MarshMallowNetworkTrackerImpl
            else -> PreviousVersionNetworkTrackerImpl
        }
    }

    fun isNetworkAvailable(connectivityManager: ConnectivityManager):Boolean{
        return NetworkStateTrackerImpl.isNetworkAvailable(connectivityManager)
    }


    //private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    internal interface NetworkStateTrackerBase{
        fun isNetworkAvailable(connectivityManager:ConnectivityManager):Boolean
    }

    //Android Version lower than MashMallow
    object PreviousVersionNetworkTrackerImpl:NetworkStateTrackerBase{
        @Suppress("DEPRECATION")
        override fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    object MarshMallowNetworkTrackerImpl:NetworkStateTrackerBase{
        @SuppressLint("SupportAnnotationUsage", "ObsoleteSdkInt")
        @RequiresApi(Build.VERSION_CODES.M)
        override fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
            return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        }

    }
    /*fun isInternetAvailable():Boolean{
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        networkCapabilities?.let{
            return when{
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i(TAG, "isInternetAvailable: true by WIFI")
                    true
                }
                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->{
                    Log.i(TAG, "isInternetAvailable: true by Cellular")
                    true
                }
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i(TAG, "isInternetAvailable: true by Ethernet")
                    true
                }
                else -> {
                    Log.i(TAG, "isInternetAvailable: false")
                    false
                }
            }
        }
        return false
    }
*/

}