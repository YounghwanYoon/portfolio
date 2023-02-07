package com.example.portfolio.feature_shopping.presentation.utils

sealed class Screen(val rout:String){
    object Splash: Screen (rout= "splash_screen")
    object Main: Screen(rout = "main_screen")
    object Detail: Screen(rout = "detail_screen/{id}")
    object Cart: Screen(rout = "cart_screen")
}
