package com.example.portfolio.feature_shopping.presentation.utils

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.portfolio.feature_shopping.presentation.cart.ShoppingCartScreen
import com.example.portfolio.feature_shopping.presentation.detail.ShoppingDetailScreen
import com.example.portfolio.feature_shopping.presentation.main.ShoppingItemStateViewModel
import com.example.portfolio.feature_shopping.presentation.main.ShoppingMainScreen
import com.example.portfolio.feature_shopping.presentation.splash.Shopping_SplashScreen

@Composable
fun setNavGraph(navController: NavHostController){

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.rout
    ){

        composable(route = Screen.Main.rout){navBackStackEntry->
            val viewModel:ShoppingItemStateViewModel = hiltViewModel(navBackStackEntry)
            ShoppingMainScreen(
                navController = navController,
                viewModel = viewModel
            )
            println("Home Button Clicked - from Nav Controller")
        }

        composable(route = Screen.Cart.rout){ navBackStackEntry->
            val viewModel:ShoppingItemStateViewModel = hiltViewModel(navController.previousBackStackEntry!!)
            ShoppingCartScreen(
                navController = navController,
                viewModel = viewModel
            )
            println("Cart Button Clicked - from Nav Controller")
        }

        composable(
            route = Screen.Detail.rout,
            arguments = listOf(navArgument("id"){
                type = NavType.IntType
            })){ navBackStackEntry ->
            val viewModel:ShoppingItemStateViewModel = hiltViewModel(navController.previousBackStackEntry!!)

            ShoppingDetailScreen(
                navController = navController,
                selectedItemId = navBackStackEntry.arguments?.getInt("id").toString(),
                viewModel = viewModel
            )
        }

        composable(route = Screen.Splash.rout){
            Shopping_SplashScreen(navController = navController)
        }
    }

}