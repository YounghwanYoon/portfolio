package com.example.portfolio.feature_shopping.presentation.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.portfolio.feature_shopping.presentation.main.MyDivider
import com.example.portfolio.feature_shopping.presentation.utils.ShoppingColors
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Preview(
    showSystemUi = false,
    showBackground = true,
    name = "Preview_PaymentScreen",
    group = "",
    widthDp = 360,
    heightDp = 640,
    locale = "us",
    backgroundColor = 0,
    uiMode = 0,
    device = ""

)
@Composable
fun PaymentScreen(modifier:Modifier=Modifier){
    PaymentUIDialog()
}


//https://stackoverflow.com/questions/68852110/show-custom-alert-dialog-in-jetpack-compose
@Composable
fun CustomPaymentDialog(openDialogCustom: MutableState<Boolean> = mutableStateOf(false), title:String ="Order Summary"){
    Dialog(
        onDismissRequest = {openDialogCustom.value = false}
    ){
        PaymentUIDialog(openDialogCustom = openDialogCustom)
    }
}

@Composable
fun PaymentUIDialog(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean> = mutableStateOf(false),
    onCompleted:MutableState<Boolean> = mutableStateOf(false),
    bodyContent:@Composable () -> Unit = {ViewPager()},

){
    Card(
        modifier = modifier
            .padding(horizontal =10.dp, vertical =5.dp)
            .fillMaxSize()
            .background(color = Color.Black)
            //.align(Alignment.Center)
        ,
        shape = RoundedCornerShape(10.dp),
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Column(
                modifier = Modifier.weight(0.1f). padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp),
                    text = "Title Section"
                )
                MyDivider(
                    modifier = Modifier.fillMaxWidth(0.85f).align(Alignment.CenterHorizontally),
                    height = 2.dp
                )
            }

            Row(
                modifier= Modifier.weight(0.8f)
                    .fillMaxWidth()
            ){
                bodyContent()
            }

            Row(
                modifier= Modifier.weight(0.1f)
                    .fillMaxWidth()
                    .background(
                        color = if(!onCompleted.value) ShoppingColors.LightGrey else ShoppingColors.Brown_50),
                    horizontalArrangement = Arrangement.Center
            ){
                TextButton(
                    modifier = Modifier.fillMaxSize(),
                    enabled = if(onCompleted.value == false) false else true,
                    onClick = {onCompleted.value = false},
                ){
                    Text(
                        text = "Complete Order",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize= 26.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

//reference : https://medium.com/@prashantappdeveloper/viewpager-in-jetpack-compose-with-dot-indicators-within-minutes-a2779970534e
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ViewPager(
    modifier:Modifier = Modifier,
    page1:@Composable ()->Unit = {Text(modifier = Modifier.fillMaxSize(.8f), text = "Page 1")},
    page2:@Composable ()->Unit = {Text(modifier = Modifier.fillMaxSize(), text = "Page 2")},
    composableList:List<@Composable () ->Unit> = listOf(
        {Text(modifier = Modifier.fillMaxHeight(0.8f), text = "Page 1")},
        {Text(modifier = Modifier.fillMaxHeight(0.8f), text = "Page 2")},
    )
){
    val list= listOf(page1, page2)
    //val slideContent = remember { mutableStateOf(Text("Slide")) }
    val state = rememberPagerState(initialPage = 0)
    HorizontalPager(count = composableList.size, state = state) { page ->
/*        when(page)  {

            0 -> {
                slideContent.value = Text("Slide 1")
            }

            1 -> {
                slideContent.value = Text("Slide 2")
            }

        }*/

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when(page){
                0->{composableList[0]()}
                1->{composableList[1]()}
            }
        }

    }

    Spacer(modifier = Modifier.padding(4.dp))

    DotsIndicator(
        totalDots = composableList.size,
        selectedIndex = state.currentPage,
        selectedColor = Color.Red,
        unSelectedColor = Color.Gray
    )
}
@Composable
fun DotsIndicator(
    totalDots : Int,
    selectedIndex : Int,
    selectedColor: Color,
    unSelectedColor: Color,
){

    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()

    ) {

        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(unSelectedColor)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}