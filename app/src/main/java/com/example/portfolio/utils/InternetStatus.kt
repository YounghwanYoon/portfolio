package com.example.demoapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log

class InternetStatus {

    companion object{
        private val TAG:String = this.javaClass.name

        fun isInternetAvailable(context:Context): Boolean{

            var result:Boolean = false

            val cm:ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(cm != null){
                    val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                    if(networkCapabilities != null){
                        if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                            Log.i(TAG, "isInternetAvailable: by TRANSPORT_CELLULAR ")
                            result = true
                        }
                        else if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                            Log.i(TAG, "isInternetAvailable: by TRANSPORT_WIFI ")
                            result = true
                        }
                        else if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                            Log.i(TAG, "isInternetAvailable: by TRANSPORT_ETHERNET ")
                            result = true
                        }
                        else if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                            Log.i(TAG, "isInternetAvailable: by TRANSPORT_VPN ")
                            result = true
                        }
                    }
                }
            }else{
                if(cm != null){
                    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                    if(activeNetwork != null){
                        when (activeNetwork.type){
                            ConnectivityManager.TYPE_WIFI -> result = true
                            ConnectivityManager.TYPE_MOBILE -> result = true
                            ConnectivityManager.TYPE_ETHERNET -> result = true
                            ConnectivityManager.TYPE_VPN -> result = true
                        }

                    }
                }

            }

            return result
        }

    }

}