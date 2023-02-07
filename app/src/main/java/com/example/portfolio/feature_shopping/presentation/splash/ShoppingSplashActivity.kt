package com.example.portfolio.feature_shopping.presentation.splash

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.presentation.ui.theme.Brown_700
import com.example.portfolio.feature_shopping.presentation.ui.theme.ShoppingTheme
import com.example.portfolio.feature_shopping.presentation.utils.ShoppingColors
import com.example.portfolio.utils.permissionHandlerCompose
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
/*

@SuppressLint("CustomSplashScreen")
class ShoppingSplashActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
*/
/*
                val multiplePermissionsState = rememberMultiplePermissionsState(
                    listOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                    )
                )
                CheckPermissionState(multiplePermissionsState)
 *//*


        lifecycleScope.launchWhenCreated {
            setContent {
                //SplashScreen.SPLASH_SCREEN_STYLE_ICON
                ShoppingTheme {

                    SplashContents(
                        modifier = Modifier.background(color = Brown_700)
                    )
                    //SplashScreen()
                }
            }
            //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { }

*/
/*
            delay(2500)
           val intent = Intent(this@ShoppingSplashActivity, ShoppingMain::class.java)
            startActivity(intent)

            finish()

 *//*

        }
    }

    */
/*  override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return ComposeView(this).apply {
            lifecycleScope.launchWhenCreated {
                delay(3000)
                val intent = Intent(this@ShoppingSplashActivity, ShoppingDemo::class.java)
                startActivity(intent)
                finish()

            }
        }
    }*//*



    */
/**
     * remember save UI state while in use but it will be reset when it rotated or restarting activity.
     * rememberSaveable save UI state and it won't reset while rotated or on pause/restore
     * using 'by' will let you delegate to skip using '.value'
     *//*

}
*/

//Splash Screen
@Composable
fun Shopping_SplashScreen(
    color: Color = ShoppingColors.LightColors.primaryVariant,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val painter = painterResource(R.drawable.coffee_bean_falling)
        Image(
            modifier= Modifier
                //.weight(0.7f, fill = false)
                .weight(.6f, fill = true)
                //.aspectRatio(ratio = painter.intrinsicSize.width / painter.intrinsicSize.height)
                .fillMaxWidth()
                .fillMaxHeight(),

            painter = painter,
            contentDescription = "Opening Loading Coffee Image",
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .weight(.32f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
            //.background(color = Color.Black),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {

                androidx.compose.material.Text(
                    "Roasting Coffee Beans...",
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier=Modifier.height(16.dp))

                CircularProgressAnimated()
            }
        }
    }
}


@Composable
private fun CircularProgressAnimated() {
    val progressValue = 0.99f
    val infiniteTransition = rememberInfiniteTransition()

    val progressAnimationValue by infiniteTransition.animateFloat(
        initialValue = 0.0f,
        targetValue = progressValue,
        animationSpec = infiniteRepeatable(
            animation = tween(900)
        )
    )

    CircularProgressIndicator(
        modifier = Modifier.size(75.dp),
        progress = progressAnimationValue,
        color = ShoppingColors.LightColors.primary
    )
}


@Composable
private fun SlideInAnimationScreen(
    animationDuration:Int = 2000,
    screen:Unit
){
    var visible by remember { mutableStateOf(false) }
    AnimatedVisibility(
        visible = visible,
        enter= fadeIn(
            animationSpec = tween(durationMillis = animationDuration)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis =  animationDuration)
        )
    ){
        screen
    }


}
