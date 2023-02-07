package com.example.portfolio.feature_shopping.presentation.utils

import com.example.portfolio.feature_shopping.presentation.main.ShoppingMainScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.portfolio.feature_shopping.presentation.cart.ShoppingCartScreen
import com.example.portfolio.feature_shopping.presentation.detail.ShoppingDetailScreen
import com.example.portfolio.feature_shopping.presentation.splash.Shopping_SplashScreen

@Composable
fun setNavGraph(navController: NavHostController){

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.rout
    ){

        composable(route = Screen.Main.rout){
            ShoppingMainScreen(navController = navController)
            println("Home Button Clicked - from Nav Controller")
        }

        composable(route = Screen.Cart.rout){
            ShoppingCartScreen(navController = navController)
            println("Cart Button Clicked - from Nav Controller")
        }

        composable(
            route = Screen.Detail.rout,
            arguments = listOf(navArgument("id"){
                type = NavType.IntType
            })
        ){
            ShoppingDetailScreen(
                navController = navController,
                selectedItemId = it.arguments?.getInt("id").toString()
            )
        }



        composable(route = Screen.Splash.rout){
            Shopping_SplashScreen(navController = navController)
        }
    }

}