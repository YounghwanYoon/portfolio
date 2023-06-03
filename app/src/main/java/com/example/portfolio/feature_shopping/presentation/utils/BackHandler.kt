package com.example.portfolio.feature_shopping.presentation.utils

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.portfolio.feature_shopping.presentation.utils.Screens


@Composable
fun BackStateHandler(navController: NavController, activity:Activity){
    println( "BackStateHandler: from ${navController.currentDestination?.route}")
    val caller = remember{navController.currentDestination!!.route}
    BackHandler(enabled = true) {
        when{
            (Screens.Splash.rout == caller) -> {
                activity.finish()
            }
            (Screens.Main.rout == caller) -> {
                activity.finish()
            }
            (Screens.Detail.rout == caller) -> {
                navController.navigate(Screens.Main.rout)
            }
            (Screens.Cart.rout == caller) -> {
                navController.navigate(Screens.Cart.rout)
            }

        }
    }

}