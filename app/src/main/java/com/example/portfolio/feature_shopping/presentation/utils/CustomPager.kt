package com.example.portfolio.feature_shopping.presentation.utils

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.presentation.main.EachItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.absoluteValue

@Preview
@Composable
fun PagerAndButtonTester(){
    AutomaticPager()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutomaticPager(
    modifier: Modifier = Modifier,
    isUserPressing:Boolean = false,
    action:()->Unit = {},
    specialItems: List<SpecialItem>? = listOf(
        SpecialItem(
            id = 0,
            image = R.drawable.coffee_animation,
            description = "1",
            title = "first",
            start_date = "",
            end_date = ""
        ),
        SpecialItem(
                id = 1,
                image = R.drawable.coffee_animation,
                description = "2",
                title = "second",
                start_date = "",
                end_date = ""
        ),
        SpecialItem(
                id = 2,
                image = R.drawable.coffee_animation,
                description = "3",
                title = "third",
                start_date = "",
                end_date = ""
            )
        ),
){
    val padding = 24.dp
    val itemWidth = 360.dp
    val itemHeight = (itemWidth.value * 0.80).dp //300.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val contentPadding = PaddingValues(start = padding, end = padding)// (screenWidth - itemWidth + horizontalPadding))
    val pagerState = rememberPagerState()

    val items by remember{mutableStateOf(specialItems)}

    val pauseState = remember{mutableStateOf(false)}
    var userPressed by remember{mutableStateOf(isUserPressing)}

    Column(modifier = modifier.background(color= Color.White)){
        HorizontalPager(
            modifier = Modifier.size(width= itemWidth, height = itemWidth),
            count = items?.size ?: 0,
            state = pagerState,
            itemSpacing = 1.dp,
            contentPadding = contentPadding //PaddingValues( start = 24.dp, end = 24.dp)
        ) { page ->

            if(items == null){
                CircularProgressIndicator()
            }else {
                // Our page content
                val curItem = items!![pagerState.currentPage]
                EachItem(
                    modifier = Modifier.graphicsLayer {
                        val pageOffset =
                            ((pagerState.currentPage - page) + pagerState.currentPageOffset)
                                .absoluteValue
                        // We animate the alpha, between 50% and 100%
                        alpha = androidx.ui.lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                    },
                    painter =
                    //Image data from Server
                    if (curItem.imageUrl != "") rememberAsyncImagePainter(curItem.imageUrl)
                    //Image for local use for design
                    else painterResource(curItem.image),
                    contentDescription = curItem.description,
                    text = curItem.description,
                    onTouch = { userPressed = true }
                )
            }
        }
        if(items !=null){

            val coroutineScope = rememberCoroutineScope()

            val initalDelay = 3000L
            val period = 5000L

            LaunchedEffect(key1 = true){
                timer(initialDelay = initalDelay, period = period){
                    coroutineScope.launch {
                        if(!pauseState.value){
                            when{
                                userPressed ->{
                                    delay(5000L)
                                    userPressed = false
                                }
                                pagerState.currentPage < items!!.size-1 ->{
                                    pagerState.animateScrollToPage(page = pagerState.currentPage +1)
                                }
                                pagerState.currentPage == items!!.size-1 ->{
                                    pagerState.animateScrollToPage(page = 0)
                                }
                            }
                        }else{
                            //Do pause
                        }
                        println("running")
                    }
                }
            }
        }
    }
}

@Composable
fun PagerPlayButtons(
    modifier:Modifier = Modifier,
    onPlayClick:()->Unit = {},
    onPauseClick:()->Unit = {},
    onForwardClick:()->Unit ={},
    isStop: MutableState<Boolean> = mutableStateOf(value = false)
){
    val shouldStop = remember{
        isStop
    }
    println(shouldStop)

    Row(modifier= modifier){

        IconButton(
            onClick = {
                println("pause btn is clicked $shouldStop")
                shouldStop.value =!shouldStop.value
                println("pause btn is clicked $shouldStop")
            },
            content = {
                Crossfade(targetState = shouldStop.value){
                    when(it){
                        true ->{
                            Icon(
                                painter =  painterResource(R.drawable.baseline_pause_24),
                                contentDescription = "play_btn_image",
                            )
                        }
                        false -> {
                            Icon(
                                painter = painterResource(R.drawable.baseline_play_arrow_24),
                                contentDescription = "play_btn_image",
                            )
                        }
                    }
                }
            }
        )


        Spacer(modifier = Modifier.width(4.dp))
        IconButton(
            onClick = {onForwardClick()}
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_fast_forward_24),
                contentDescription = "next_btn_image",
            )
        }
    }
}