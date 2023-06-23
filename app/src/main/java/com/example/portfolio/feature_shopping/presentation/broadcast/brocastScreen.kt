package com.example.portfolio.feature_shopping.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


@Composable
fun BroadcastScreen(
    modifier: Modifier = Modifier,
    systemAction:String,
    onSystemEvent:(intent: Intent?) -> Unit
){
    val context = LocalContext.current
    val currentOnSystemEvent by rememberUpdatedState(newValue = onSystemEvent)

    DisposableEffect(key1 = context, key2= systemAction){
        val intentFilter = IntentFilter(systemAction)
        val broadcastReceiver = object: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnSystemEvent(intent)
            }
        }
        context.registerReceiver(broadcastReceiver, intentFilter)

        onDispose{
            context.unregisterReceiver(broadcastReceiver)
        }


    }
    //push notification - by server
    //local notification - use notification channel
}

@Composable
fun BroadcastContent(modifier:Modifier = Modifier, text:String = "Thank you for the order. Your "){

}