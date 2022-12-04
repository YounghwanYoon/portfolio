package com.example.portfolio.feature_shopping.presentation

import android.view.Window
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.domain.model.SellingItem


@Composable
fun DetailScreen (
    modifier: Modifier = Modifier,
    screenHeight:Dp,
    screenWidth: Dp,
    window: Window
){
/*
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )*/


    Column(
        modifier= modifier.background(color = MaterialTheme.colorScheme.background)
    ){
        //Header() // Skip in this section
        Detail_Body(
            modifier = Modifier.weight(9f),
            screenHeight = screenHeight,
            screenWidth = screenWidth)
        Footer(Modifier.weight(1f))
    }

}


@Composable
fun Detail_Body(
    modifier:Modifier = Modifier.padding(16.dp),
    painter: Painter = painterResource(R.drawable.coffee_animation),
    data: SellingItem? = null,
    screenHeight: Dp,
    screenWidth: Dp
) {
        ImageFrame(modifier
            .fillMaxSize()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(0.dp),
            content = {
                Column(modifier.fillMaxSize()){
                    Detail_ImageAndTitle(modifier.weight(5f).padding(top = 0.dp, bottom= 0.dp))
                    Detail_ProductInfo(
                        modifier
                            .weight(5f)
                            .padding(start = 18.dp, end = 18.dp,top = 16.dp, bottom= 8.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            },
            bottomContent = {Detail_PriceFloatBtn(Modifier)}
        )

/*    Box(
        modifier = modifier
    ){
        //MugFrame with Image
        ImageFrame(Modifier.fillMaxSize())
        //MugFrame

            Column(
                modifier = Modifier.padding(0.dp).background(color=MaterialTheme.colorScheme.primary)
            ){
                Card(
                    modifier = Modifier.weight(4f).background(color=MaterialTheme.colorScheme.primary),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().background(color=MaterialTheme.colorScheme.primary),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BorderImage(
                            Modifier
                            .padding(8.dp)
                            .weight(8f)
                        )
                        Text(
                            modifier=Modifier.weight(2f),
                            text = "Seasonal Spice",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Text( text = "Country:America",
                    modifier = Modifier
                        .weight(5f)
                        .padding(start=16.dp)
                )
                MyDivider()
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ){
                    Text("$6.99")
                }
            }
        }*/

}

/**
 * Simply
 */
@Composable
fun ImageFrame(
    modifier:Modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
    painter:Painter = painterResource(R.drawable.coffee_mug_cup_frame),
    content: @Composable () -> Unit,
    bottomContent: @Composable () -> Unit = {},
){
    Column(modifier = modifier.background(color = MaterialTheme.colorScheme.background)){
        Surface(
            //modifier.background(color = MaterialTheme.colorScheme.background),
            color= MaterialTheme.colorScheme.background
        ){
            Card (
                modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
                elevation = 6.dp,
            ){
                Image(
                    //modifier= Modifier.background(color = MaterialTheme.colorScheme.primary),
                    painter = painter,
                    contentDescription = "Mug Cup ImageFrame",
                )
                Column(
                    modifier = Modifier
                        .padding(top = 28.dp, bottom= 8.dp, start = 8.dp, end = 18.dp),
                ){
                    Box(modifier = Modifier.weight(8.9f)){
                        content()
                    }

                    MyDivider(height = 0.dp)

                    Box(modifier = Modifier.weight(1.1f).padding(top = 0.dp, bottom = 0.dp)){
                        bottomContent()
                    }
                }
            }
        }

    }
}

@Composable
fun Detail_ImageAndTitle(
    modifier:Modifier = Modifier,
    painter:Painter = painterResource(R.drawable.coffee_animation),
    description:String = "Coffee Image",
    itemTitle:String = "American Coffee"
){
    Box(modifier = modifier){
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ){
                Card(
                    elevation = 8.dp,
                    modifier = Modifier
                        .weight(7f)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onTertiary,
                            shape = RoundedCornerShape(6.dp)
                        )
                ){
                    Image(
                        painter =painter,
                        contentDescription = description,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = itemTitle,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

    }
}

@Composable
fun Detail_ProductInfo(modifier:Modifier = Modifier.verticalScroll(rememberScrollState()), data:SelectedData = SelectedData(1)){

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
    ){
        Text("Country:  ${data.country}")
        Spacer(modifier.height(4.dp))
        Text("Bean Type:  ${
            when (data.type) {
                BeanType.Espresso -> {"Espresso Roast"}
                BeanType.Dark -> {"Dark Roast"}
                BeanType.Medium -> {"Medium Roast"}
                BeanType.Light -> {"Light Roast"}
                else -> {"Dark Roast"}
            }
        }")
        Spacer(modifier.height(4.dp))
        Text("Information: \n   ${data.information}")
        Spacer(modifier.height(8.dp))
    }
}

@Composable
fun Detail_PriceFloatBtn(modifier:Modifier = Modifier, price:String = "$6.99"){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(modifier = Modifier.weight(8f).padding(start=16.dp)){Text(text=price)}
        Box(modifier = Modifier.weight(2f)){ Shopping_FloatBtn()}
    }
}

@Composable
fun Shopping_FloatBtn(modifier:Modifier = Modifier, onClick:() -> Unit = {}){
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        contentColor = MaterialTheme.colorScheme.tertiary,
        backgroundColor = MaterialTheme.colorScheme.onTertiary
    ){
        Icon(Icons.Filled.Add, "Adding Button with Plus Icon")
    }
}

data class SelectedData(
    val id:Int = 0,
    val country:String = "North America",
    val type:BeanType = BeanType.Dark,
    val information:String =
            "Once upon a time, there was a coffee bean grow from Northern America." +
            "It was so flavorful and gave so much energy to people whoever had a chance to sip of it" +
            "This was one of many reasons how American worked harder than other countries."


)
enum class BeanType{
    Espresso, Dark, Medium, Light
}
/*
@Composable
fun BorderImage(modifier:Modifier = Modifier, painter:Painter = painterResource(R.drawable.coffee_animation)){
    Card(
        modifier = modifier,
        border = BorderStroke(width= 2.dp, color = MaterialTheme.colorScheme.onTertiary)
    ){
        Image(
            painter = painter,
            contentDescription = "Image with Border",
            contentScale = ContentScale.Fit
        )
    }
}*/
