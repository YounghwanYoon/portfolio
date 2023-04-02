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
import com.example.portfolio.feature_shopping.presentation.search.SearchScreen
import com.example.portfolio.feature_shopping.presentation.search.SearchViewModel
import com.example.portfolio.feature_shopping.presentation.splash.Shopping_SplashScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun setNavGraph(navController: NavHostController, isTablet:Boolean){

    val itemStateViewModel:ShoppingItemStateViewModel = hiltViewModel()
    val cartStateViewModel: CartStateViewModel = hiltViewModel()
    val isTablet = remember{isTablet}
    NavHost(
        navController = navController,
        startDestination = Screens.Splash.rout//Screens.Splash.rout
    ){
        composable(route = Screens.Main.rout){ navBackStackEntry->
            //val viewModel:ShoppingItemStateViewModel = hiltViewModel(navBackStackEntry)
            ShoppingMainScreen(
                navController = navController,
                itemStateVM = itemStateViewModel,
                cartStateVM = cartStateViewModel,
                tabletState = isTablet
            )
            println("Home Button Clicked - from Nav Controller")
        }

        composable(route = Screens.Search.rout){navBackStackEntry ->
            val searchStateViewModel:SearchViewModel = hiltViewModel()

            SearchScreen(
                navController = navController,
                onBackClicked ={navController},
                searchVM = searchStateViewModel,
                isTablet = isTablet
            )
        }

        composable(route = Screens.Cart.rout){ navBackStackEntry->

            val parentEntry  = remember(navBackStackEntry){
                navController.getBackStackEntry(Screens.Main.rout)
            }

           // val viewModel:ShoppingItemStateViewModel = hiltViewModel(navController.previousBackStackEntry!!)
            ShoppingCartScreen(
                navController = navController,
                cartStateViewModel = cartStateViewModel,
                isTablet = isTablet
            )
            println("Cart Button Clicked - from Nav Controller")
        }

        composable(
            route = Screens.Detail.rout + "/{id}",
            arguments =
                listOf(
                    navArgument("id"){
                         type = NavType.StringType
                    }
                )
        ){ navBackStackEntry ->
                    val parentEntry  = remember(navBackStackEntry){
                        navController.getBackStackEntry(Screens.Main.rout)
                }
            //val viewModel:ShoppingItemStateViewModel = hiltViewModel(navController.previousBackStackEntry!!)
            ShoppingDetailScreen(
                navController = navController,
                selectedItemId = navBackStackEntry.arguments?.getString("id").toString(),
                itemStateVM = itemStateViewModel,
                cartStateViewModel = cartStateViewModel,
                isTablet = isTablet
            )
        }

        composable(route = Screens.Splash.rout){
            Shopping_SplashScreen(navController = navController,isTablet = isTablet)
        }
    }
}