package com.example.portfolio.feature_permission.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.ui.core.inMilliseconds
import androidx.ui.core.seconds
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import timber.log.Timber
import kotlin.time.Duration.Companion.minutes

class LocationForegroundService: Service() {

    private val CHANNEL_ID = "Foreground Service - Location"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object{
        private var listener:GPSTracker? = null

        fun startService(context: Context, message:String, gpsTracker: GPSTracker){
            val intent = Intent(context, LocationForegroundService::class.java)
            listener = gpsTracker
            ContextCompat.startForegroundService(context, intent)
        }
        fun stopService(context:Context){
            val intent = Intent(context,LocationForegroundService::class.java)
            context.stopService(intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag("LocationForegroundService").d("onStartCommand: is running")
        createNotificationChannel()
        requestLocationUpdate()
        val notification = buildNotification()
        startForeground(123, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate(){
        val locationInterval = 1.minutes.inWholeMilliseconds
        val locationFastestInterval = 5.seconds.inMilliseconds()
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationInterval)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(locationInterval)
            .build()

        val client:FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if(permissionState(Manifest.permission.ACCESS_FINE_LOCATION) ||
            permissionState(Manifest.permission.ACCESS_COARSE_LOCATION)){

            client.requestLocationUpdates(
                locationRequest,
                object: LocationCallback(){
                    override fun onLocationAvailability(p0: LocationAvailability) {
                        super.onLocationAvailability(p0)
                    }

                    override fun onLocationResult(locationResult: LocationResult) {
                        val location = locationResult.lastLocation
                        if(location != null){
                            val lattitude = location.latitude
                            val longititude = location.longitude
                            listener?.onLocationReceived(lattitude = lattitude, longitude = longititude)
                        }
                        super.onLocationResult(locationResult)
                    }
                },
                null
            )
        }


    }

    private fun permissionState(perm:String):Boolean{
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED
    }

    private fun buildNotification(): Notification {
        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_ID)

        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is obtaining your location")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        return notification
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

    }

}
