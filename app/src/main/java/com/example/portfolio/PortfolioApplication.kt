package com.example.portfolio

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.portfolio.feature_shopping.presentation.utils.ConfirmNotificationService
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class PortfolioApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())



        createNotificationChannel()
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                ConfirmNotificationService.CONFIRMATION_CHANNEL_ID,
                "Confirmation",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Used for the confirm order notifications"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        //below Oreo, broadcast receiver does not exists
    }

}