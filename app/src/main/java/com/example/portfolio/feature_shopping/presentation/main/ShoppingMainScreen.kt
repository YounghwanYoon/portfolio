package com.example.portfolio.feature_shopping.presentation.main

import Footer
import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberAsyncImagePainter
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.ShoppingUIEvent
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.use_case.*
import com.example.portfolio.feature_shopping.presentation.cart.CartStateViewModel
import com.example.portfolio.feature_shopping.presentation.main.*
import com.example.portfolio.feature_shopping.presentation.utils.*
import timber.log.Timber
import java.util.*


@SuppressLint("ObsoleteSdkInt")
@Composable
fun ShoppingMainScreen(
    navController: NavController,
    itemStateVM: ShoppingItemStateViewModel,
    cartStateVM: CartStateViewModel,
    tabletState:Boolean
) {
    val isTablet = remember{tabletState}
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Header(
            modifier =
                when(isTablet){
                    false ->{
                        Modifier
                            .weight(0.10f)
                            .background(color = MaterialTheme.colorScheme.background)
                    }

                    true ->{
                        Modifier
                            .weight(0.10f)
                            .background(color = MaterialTheme.colorScheme.background)
                    }

                },
            onSearchClicked = {
                navController.navigate(Screens.Search.rout) {
                    popUpTo(Screens.Search.rout)
                }
            },
            isTablet = isTablet
        )
        MyDivider()
        Body(
            modifier =
                when(isTablet){
                    false ->{
                        Modifier.weight(0.8f)
                    }
                    true ->{
                        Modifier.weight(0.7f)
                    }
                },
            navController = navController,
            itemStateVM = itemStateVM
        )
        //MyDivider()
        Footer(
            modifier = when(isTablet){
                false->{
                    Modifier
                        .weight(0.08f)
                        .defaultMinSize(minHeight = 50.dp)
                        //.fillMaxWidth()

                }
                true ->{
                    Modifier
                        .weight(0.10f)
                        .defaultMinSize(minHeight = 50.dp)
                }
            },
            navController = navController,
            cartState = cartStateVM.cartUIState
        )
    }
}

//***HEADER***
@SuppressLint("UnrememberedMutableState")
@Composable
fun Header(
    modifier: Modifier = Modifier,
    onSearchClicked: () -> Unit,
    isTablet:Boolean
) {
    Row(
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (buttonCart, logoTitle, search) = createRefs()
            when(isTablet){
                false ->{
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .constrainAs(logoTitle) {
                                absoluteLeft.linkTo(parent.start)
                                absoluteRight.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(search.top)
                            },

                        text = "Beans",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontSize = 24.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    androidx.compose.material3.IconButton(
                        modifier = Modifier.constrainAs(buttonCart) {
                            top.linkTo(logoTitle.top)
                            bottom.linkTo(logoTitle.bottom)
                            //absoluteLeft.linkTo(logoTitle.absoluteRight, margin = 8.dp)
                            absoluteRight.linkTo(parent.end, margin = 8.dp)
                        },
                        onClick = {}
                    ) {
                        androidx.compose.material3.Icon(
                            modifier = Modifier,
                            painter = painterResource(R.drawable.coffee_icon_mugcup),
                            contentDescription = "Shopping Cart"
                        )
                    }
                    //Search Bar Section

                    Spacer(modifier = Modifier.height(4.dp))
                    SearchViewBtn(
                        modifier = Modifier
                            .padding(top = 0.dp)
                            .background(
                                color = ShoppingColors.PearlWhite
                            )
                            .constrainAs(search) {
                                top.linkTo(logoTitle.bottom, margin = 4.dp)
                                bottom.linkTo(parent.bottom, margin = 0.dp)
                                absoluteLeft.linkTo(parent.absoluteLeft)
                                absoluteRight.linkTo(parent.absoluteRight)
                            }
                            .clickable {
                                onSearchClicked()
                            },
                        //onClick = onSearchClicked
                    )
                }
                true ->{
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .constrainAs(logoTitle) {
                                absoluteLeft.linkTo(parent.start)
                                absoluteRight.linkTo(search.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            },
                        text = "Beans",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontSize = 24.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    androidx.compose.material3.IconButton(
                        modifier = Modifier.constrainAs(buttonCart) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            absoluteLeft.linkTo(search.end, margin = 8.dp)
                            absoluteRight.linkTo(parent.end, margin = 8.dp)
                        },
                        onClick = {}
                    ) {
                        androidx.compose.material3.Icon(
                            modifier = Modifier,
                            painter = painterResource(R.drawable.coffee_icon_mugcup),
                            contentDescription = "Shopping Cart"
                        )
                    }
                    //Search Bar Section

                    Spacer(modifier = Modifier.height(4.dp))
                    SearchViewBtn(
                        modifier =
                            when(isTablet){
                                false ->{
                                    Modifier
                                        .padding(top = 0.dp)
                                        .background(
                                            color = ShoppingColors.PearlWhite
                                        )
                                        .constrainAs(search) {
                                            top.linkTo(parent.top, margin = 0.dp)
                                            bottom.linkTo(parent.bottom, margin = 0.dp)
                                            absoluteLeft.linkTo(parent.absoluteLeft)
                                            absoluteRight.linkTo(parent.absoluteRight)
                                        }
                                        .clickable {
                                            onSearchClicked()
                                        }
                                }
                                true ->{
                                    Modifier
                                        .defaultMinSize(minWidth = 150.dp)
                                        .background(
                                            color = ShoppingColors.PearlWhite
                                        )
                                        .constrainAs(search) {
                                            top.linkTo(parent.top, margin = 0.dp)
                                            bottom.linkTo(parent.bottom, margin = 0.dp)
                                            absoluteLeft.linkTo(parent.absoluteLeft)
                                            absoluteRight.linkTo(parent.absoluteRight)
                                        }
                                        .clickable {
                                            onSearchClicked()
                                        }
                                }
                            },
                    )
                }
            }
        }
    }
}

@Composable
fun SearchViewBtn(
    modifier: Modifier = Modifier.border(2.dp, color= Color.Black, shape = RoundedCornerShape(4.dp)),
) {
    androidx.compose.material3.Card(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            disabledContainerColor = Color.White,
            contentColor = ShoppingColors.CounterBlue
        ),
        content = {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                text = stringResource(id = R.string.searchview_text),
                fontStyle = FontStyle.Italic,
            )
        }
    )
}

@SuppressLint("ObsoleteSdkInt")
@Composable
fun Body(
    modifier: Modifier = Modifier,
    navController: NavController,
    itemStateVM: ShoppingItemStateViewModel,
) {
    Timber.d("Body()")
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val configuration = LocalConfiguration.current
    val screenHeight: Dp
    val screenWidth: Dp

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
        screenHeight = 400.dp
        screenWidth = 360.dp
    } else {
        screenHeight = configuration.screenHeightDp.dp
        screenWidth = configuration.screenWidthDp.dp
    }

    BodyContent(
        modifier,
        screenHeight = screenHeight,
        screenWidth = screenWidth,
        specialItems = itemStateVM.specialItems.collectAsStateWithLifecycle(
            lifecycle = lifecycle, minActiveState = Lifecycle.State.STARTED
        ).value,
        navController = navController,
        pagingData = itemStateVM.pager.collectAsLazyPagingItems(),
        onDetailRequested = itemStateVM::getSelectedItem,
        onUIEvent = itemStateVM::onUIEvent
    )
    //com.example.portfolio.feature_shopping.presentation.main.GridView(modifier)
}

/**
 * Ref: https://stackoverflow.com/questions/70590404/jetpack-compose-lazyverticalgrid-SellingItem-throws-java-lang-illegalstateexception
 * Any extra composables must be added outside the SellingItem block,
 * or as extra SellingItem block inside the LazyVerticalGrid()
 * will cause Error : Place was called on a node which was placed already
 */
@Composable
fun BodyContent(
    modifier: Modifier = Modifier,
    specialItems: List<SpecialItem>,
    screenHeight: Dp = 360.dp,
    screenWidth: Dp = 640.dp,
    navController: NavController,
    pagingData: LazyPagingItems<SellingItem>,
    onDetailRequested: (id:Int)->Unit,
    onUIEvent:(event: ShoppingUIEvent) -> Unit
) {
    val config = LocalConfiguration.current
    val deviceWidth = config.screenWidthDp.dp
    val deviceHeight = config.screenHeightDp.dp

    Timber.d("current width & height $deviceWidth, $deviceHeight ")
    val itemWidth:Dp
    val itemHeight:Dp
    val span:GridItemSpan
    val cells:Int
    //when rotated or tablet
    when(deviceWidth > deviceHeight || deviceWidth > 415.dp){
        false ->{
            itemWidth = (deviceWidth.value * 0.95f).dp
            itemHeight = (deviceHeight.value * 0.40f).dp
            cells = 2
            span = GridItemSpan(cells)

        }
        true ->{
            itemWidth = (deviceWidth.value * 0.60f).dp
            itemHeight = (deviceHeight.value * 0.70f).dp
            cells = 3
            span = GridItemSpan(cells)
        }
    }
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(cells),
        contentPadding = PaddingValues(start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp),
        userScrollEnabled = true,
    ){
        //First Section Title
        item(
            span = {span}
        ){
            Text(
                text = "Season Special",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
            )
        }
        //First Section Content
        item(
            span = {span}
        ){
            AutomaticPager(
                modifier = Modifier
                    .width(itemWidth)
                    .height(itemHeight),
                specialItems = specialItems
            )
        }
        //Section Item
        //Start with title
        item(
            span = {span}
        ) {
            Text(
                text = "Regular Items",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(
            //span = {span},
            count = pagingData.itemCount,
            key = pagingData.itemKey { it.id },
        ){itemIndex->
            val item = pagingData[itemIndex]
            EachItem(
                modifier = Modifier
                    .width(
                        //Rotated
                        if (screenWidth > screenHeight) screenWidth.times(0.27f) else screenWidth.times(
                            0.48f
                        )
                    )
                    .height(
                        if (screenWidth > screenHeight) screenHeight.times(0.48f) else screenHeight.times(
                            0.27f
                        )
                    )
                    .clickable {
                        Timber.d("clicked item")
                        item?.let{
                            onDetailRequested(it.id)
                            onUIEvent(ShoppingUIEvent.RequestedDetail(selectedID = it.id))
                            navController.navigate(route = Screens.Detail.withArgs("${item.id}"))
                        }
                    },
                painter = if (item?.imageUrl != null || item?.imageUrl != "") rememberAsyncImagePainter(model = item?.imageUrl)
                //Image for local use for design
                else painterResource(item.image),
                text = item!!.title//item.description
            )
        }
    }

}

@Composable
fun EachItem(
    modifier: Modifier,
    painter: Painter = painterResource(R.drawable.coffee_animation),
    contentDescription: String = "null",
    text: String = "Title of Item",
) {
    val radius = 10.dp
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(radius),
        elevation = 8.dp
    ){
        Box {
            ConstraintLayout(
            ) {
                val (imageLayout, textLayout) = createRefs()
                //handle local image & server image
                Image(
                    modifier = Modifier
                        .padding(8.dp)
                        .constrainAs(imageLayout) {
                            top.linkTo(parent.top)
                        }
                        .fillMaxWidth()
                        .fillMaxSize(),

                    contentScale = ContentScale.FillBounds,
                    painter = painter,
                    contentDescription = contentDescription
                )

                Text(
                    text = text,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                        .background(ShoppingColors.LightColors.primary)
                        .constrainAs(textLayout) {
                            bottom.linkTo(parent.bottom, margin = 8.dp)
                        },
                )
            }
        }
    }
}
