package com.example.portfolio.feature_shopping.presentation.utils

sealed class Screens(val rout:String){
    object Splash: Screens (rout= "splash_screen")
    object Main: Screens(rout = "main_screen")
    object Search: Screens(rout = "search_screen")
    object Detail: Screens(rout = "detail_screen/{id}")
    object Cart: Screens(rout = "cart_screen")

    fun withArgs(vararg args:String):String{
        return buildString{
            append(rout)
            args.forEach{arg ->
                append("/$arg")
            }
        }
    }
}
