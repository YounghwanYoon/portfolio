package com.example.portfolio.feature_shopping.presentation.utils

import android.app.Activity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.portfolio.feature_shopping.presentation.cart.CartStateViewModel
import com.example.portfolio.feature_shopping.presentation.cart.ShoppingCartScreen
import com.example.portfolio.feature_shopping.presentation.detail.ShoppingDetailScreen
import com.example.portfolio.feature_shopping.presentation.main.ShoppingItemStateViewModel
import com.example.portfolio.feature_shopping.presentation.main.ShoppingMainScreen
import com.example.portfolio.feature_shopping.presentation.splash.Shopping_SplashScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun setNavGraph(navController: NavHostController){

    val itemStateViewModel:ShoppingItemStateViewModel = hiltViewModel()
    val cartStateViewModel: CartStateViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screens.Cart.rout//Screens.Splash.rout
    ){

        composable(route = Screens.Main.rout){ navBackStackEntry->


            //val viewModel:ShoppingItemStateViewModel = hiltViewModel(navBackStackEntry)
            ShoppingMainScreen(
                navController = navController,
                itemStateVM = itemStateViewModel,
                cartStateVM = cartStateViewModel
            )
            println("Home Button Clicked - from Nav Controller")
        }

        composable(route = Screens.Cart.rout){ navBackStackEntry->

/*            val parentEntry  = remember(navBackStackEntry){
//                navController.getBackStackEntry(Screens.Main.rout)
            }*/

           // val viewModel:ShoppingItemStateViewModel = hiltViewModel(navController.previousBackStackEntry!!)
            ShoppingCartScreen(
                navController = navController,
                cartStateViewModel = cartStateViewModel
            )
            println("Cart Button Clicked - from Nav Controller")
        }

        composable(
            route = Screens.Detail.rout,
            arguments = listOf(navArgument("id"){
                type = NavType.IntType
            })){ navBackStackEntry ->
            //val viewModel:ShoppingItemStateViewModel = hiltViewModel(navController.previousBackStackEntry!!)
            ShoppingDetailScreen(
                navController = navController,
                selectedItemId = navBackStackEntry.arguments?.getInt("id").toString(),
                itemStateVM = itemStateViewModel,
                cartStateViewModel = cartStateViewModel
            )
        }

        composable(route = Screens.Splash.rout){
            Shopping_SplashScreen(navController = navController)
        }
    }

    //BackStateHandler(navController = navController, activity = LocalContext.current as Activity)

}