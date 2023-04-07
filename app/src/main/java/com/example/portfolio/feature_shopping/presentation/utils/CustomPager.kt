package com.example.portfolio.feature_shopping.presentation.utils


import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.portfolio.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer


@Preview
@Composable
fun PagerAndButtonTester(){
    PagerAndButtons()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerAndButtons(
    modifier: Modifier = Modifier,
    isUserPressing:Boolean = false,
    action:()->Unit = {

    }
){
    val pagerState = rememberPagerState()
    val pauseState = remember{mutableStateOf(false)}
    var userPressed by remember{mutableStateOf(isUserPressing)}

    Column(modifier = modifier.background(color= Color.White)){
        HorizontalPager(count = 5, state = pagerState) { page ->
            // Our page content
            Text(
                text = "Page: $page",
                color= Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable {
                        userPressed = true
                    }
            )
        }

        val coroutineScope = rememberCoroutineScope()

        val initalDelay = 3000L
        val period = 5000L

        LaunchedEffect(key1 = true){
           timer(initialDelay = initalDelay, period = period){
                coroutineScope.launch {
                    when{
                        userPressed ->{
                            delay(5000L)
                        }
                        pagerState.currentPage < 4 ->{
                            pagerState.animateScrollToPage(page = pagerState.currentPage +1)
                        }
                        pagerState.currentPage == 4 ->{
                            pagerState.animateScrollToPage(page = 0)
                        }

                    }
                    println("running")
                }
            }
        }

        PagerPlayButtons(
            //isStop = pauseState,
            onPlayClick = {
                pauseState.value = !pauseState.value
                println("pauseState $pauseState")

            },
            onPauseClick = {
                pauseState.value = !pauseState.value
            },
            onForwardClick = {
            }
        )
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
                                painter = painterResource(R.drawable.baseline_play_arrow_24),
                                contentDescription = "play_btn_image",
                            )
                        }
                        false -> {
                            Icon(
                                painter =  painterResource(R.drawable.baseline_pause_24),
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