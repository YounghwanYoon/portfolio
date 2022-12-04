package com.example.portfolio.feature_shopping.presentation

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.portfolio.R
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.portfolio.feature_practice.presentation.ui.theme.JetpackComposeTheme
import com.example.portfolio.feature_shopping.presentation.ui.theme.Brown_700

@Preview (
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
fun PreviewSplashScreen(){
    JetpackComposeTheme {
        CustomSplashScreen()
    }
}
@Composable
fun CustomSplashScreen(){
    SplashContents(
        modifier = Modifier.background(color = Brown_700)
    )
}


@Composable
fun SplashContents(
    modifier:Modifier = Modifier,
    painter: Painter = painterResource(R.drawable.coffee_bean_falling),
    welcomeString: String = stringResource(R.string.splash_view_with_coffee_image)
){
    Column(
        modifier=modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

    ) {
        Image(
           modifier = Modifier.weight(0.55f),
           painter = painter,
           contentScale = ContentScale.Crop,
           contentDescription = stringResource(R.string.shopping_splash_content_description)
        )
        Text(
            text = stringResource(R.string.shopping_splash_loading_text),
            style  = MaterialTheme.typography.h4
        )

        CircularProgressAnimated(Modifier.size(75.dp).padding(8.dp))

    }

}

@Composable
private fun CircularProgressAnimated(modifier:Modifier = Modifier) {
    val progressValue = .90f
    val infiniteTransition = rememberInfiniteTransition()

    val progressAnimationValue by infiniteTransition.animateFloat(
        initialValue = 0.10f,
        targetValue = progressValue,
        animationSpec = infiniteRepeatable(
            animation = tween(900)
        )
    )

    CircularProgressIndicator(
        modifier = modifier,
        progress = progressAnimationValue,
        color = androidx.compose.material3.MaterialTheme.colorScheme.secondary
    )
}