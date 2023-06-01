package com.example.portfolio.feature_shopping.presentation.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.presentation.ShoppingActivity

class ConfirmNotificationService(
    private val context: Context
) {
    companion object{
        const val CONFIRMATION_CHANNEL_ID = "confirmation_channel"
    }
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(confirmationNumber:Int){
        val activityIntent = Intent(context, ShoppingActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val confirmationIntent = PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, ConfirmationNotificationReceiver::class.java),
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

        )

        val notification = NotificationCompat.Builder(context, CONFIRMATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_shopping_cart_24)
            .setContentTitle("Order Confirmation")
            .setContentText("Thank you for your order! Confirmation Number: $confirmationNumber")
            .setContentIntent(activityPendingIntent)
            .addAction(
                R.drawable.baseline_shopping_cart_24,
                "Order Confirmation",
                confirmationIntent
            )
            .build()
            //.setStyle()
        println("notification is created")

        notificationManager.notify(1, notification)
        println("notification manager notify")
    }
}

class ConfirmationNotificationReceiver():BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val service = context?.let { ConfirmNotificationService(it) }
        service?.showNotification(12345)
    }

}