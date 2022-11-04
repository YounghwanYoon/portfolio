package com.example.portfolio.utils

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


//Refer to follow site
//https://medium.com/@bazzairvine/observing-your-network-connection-with-flow-1cdedf31780c
class NetworkMonitor @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {

    @SuppressLint("ObsoleteSdkInt")
    val isConnected: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend (false)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend (false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
                .apply{
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        addCapability(NET_CAPABILITY_VALIDATED)
                    }
                }
            .addTransportType(TRANSPORT_WIFI)
            .addTransportType(TRANSPORT_CELLULAR)
            .addTransportType(TRANSPORT_ETHERNET)
            .build()

        trySend(NetworkStateTracker.isNetworkAvailable(connectivityManager))

        connectivityManager.registerNetworkCallback(request, callback)

        //when NetworkMonitor is goes out of scope
        awaitClose{
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}