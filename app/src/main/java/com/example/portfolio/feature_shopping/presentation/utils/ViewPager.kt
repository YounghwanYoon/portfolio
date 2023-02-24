package com.example.portfolio.feature_shopping.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

//reference : https://medium.com/@prashantappdeveloper/viewpager-in-jetpack-compose-with-dot-indicators-within-minutes-a2779970534e
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ViewPager(
    modifier: Modifier = Modifier,
    color: Color = ShoppingColors.Brown_300,
    contentList:List<@Composable () ->Unit> = listOf(
        { Text(modifier = Modifier, text = "Page 1") },
        { Text(modifier = Modifier, text = "Page 2") },
        { Text(modifier = Modifier, text = "Page 3") },
    )
){
    //val slideContent = remember { mutableStateOf(Text("Slide")) }
    val state = rememberPagerState(initialPage = 0)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        HorizontalPager(
            modifier = Modifier.fillMaxHeight(0.9f),
            count = contentList.size,
            state = state
        ){  page ->
            contentList[page]()
        }
        Spacer(modifier= Modifier.weight(0.5f))

        Box(
            modifier = Modifier.weight(0.5f)
        ){
            DotsIndicator(
                totalDots = contentList.size,
                selectedIndex = state.currentPage,
                selectedColor = color,
                unSelectedColor = Color.Gray
            )
        }
    }
/*


    HorizontalPager(count = contentList.size, state = state) { page ->
*/
/*        when(page)  {

        0 -> {
            slideContent.value = Text("Slide 1")
        }

        1 -> {
            slideContent.value = Text("Slide 2")
        }

    }*//*


        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when(page){
                0->{
                    contentList[0]()

                }
                1->{
                    contentList[1]()

                }
            }


        }

        Spacer(modifier = Modifier.padding(4.dp))


    }
*/

}
@Composable
fun DotsIndicator(
    totalDots : Int,
    selectedIndex : Int,
    selectedColor: Color,
    unSelectedColor: Color,
    weight:Float = 0.1f
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