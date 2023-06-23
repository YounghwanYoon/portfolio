package com.example.portfolio.feature_shopping.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.rememberNavController
import com.example.portfolio.BuildConfig
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.presentation.ui.theme.ShoppingTheme
import com.example.portfolio.feature_shopping.presentation.utils.Helper
import com.example.portfolio.feature_shopping.presentation.utils.setNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingActivity : ComponentActivity() {
    private val TAG = "ShoppingMain.kt"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val testingHiddenAPI = BuildConfig.TBDM_API_KEY
        val testingHiddenAPIToken = BuildConfig.TBDM_API_READ_ACCESS_TOKEN

        setContent {
            ShoppingTheme {
                val isTabletOrRoated=
                    if(Helper.isRoated()) true
                    else booleanResource(id = R.bool.is_tablet)
                println("is Tablet or Rotated $isTabletOrRoated")
                val color = colorResource(id = R.color.brown_300)
                val navController = rememberNavController()
                setNavGraph(navController, isTabletOrRoated)
/*
            //Handle Permissions
                val multiplePermissionsState = rememberMultiplePermissionsState(
                    listOf(
                        Manifest.permission.INTERNET
                        //Manifest.permission.READ_EXTERNAL_STORAGE,
                        //Manifest.permission.CAMERA,
                        //Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )

                permissionHandlerCompose(
                    multiplePermissionsState = multiplePermissionsState,
                    afterGrant = {
                        setNavGraph(navController)
                    },
                )*/
            }
        }
    }


}
