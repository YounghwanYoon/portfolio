package com.example.portfolio.feature_shopping.presentation.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.DrawerValue
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.portfolio.R
import com.example.portfolio.R.*
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.ShoppingUIEvent
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.use_case.*
import com.example.portfolio.feature_shopping.presentation.ShoppingListStateViewModel
import com.example.portfolio.feature_shopping.presentation.ui.theme.ShoppingTheme
import com.example.portfolio.feature_shopping.presentation.utils.Screen
import com.example.portfolio.feature_shopping.presentation.utils.ShoppingColors
import com.example.portfolio.feature_shopping.presentation.utils.setNavGraph
import com.example.portfolio.utils.Resource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

private val TAG = "ShoppingMain.kt"
@AndroidEntryPoint
class ShoppingMain : ComponentActivity() {

    //private val shoppingListStateVM by viewModels<ShoppingItemStateViewModel>()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            ShoppingTheme {
                //registerVM(shoppingListStateVM)

                val navController = rememberNavController()
                setNavGraph(navController)

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
/*        ComposeView(this).apply {
            setContent {
                com.example.portfolio.feature_shopping.presentation.main.ShoppingApp()
                ShoppingTheme {
                //com.example.portfolio.feature_shopping.presentation.main.ShoppingApp()
                val configuration = LocalConfiguration.current
                val screenHeight:Dp
                val screenWidth:Dp
                val window: Window = window

                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2 ){
                    screenHeight = 400.dp
                    screenWidth  = 360.dp
                }else {
                    screenHeight = configuration.screenHeightDp.dp
                    screenWidth  = configuration.screenWidthDp.dp
                }

               // DetailScreen(screenHeight = screenHeight, screenWidth = screenWidth, window = window)
                ShoppingCartScreen()
            }
            }
        }*/
    }
}

@Composable
fun ShoppingApp(modifier: Modifier = Modifier, navController: NavController) {

    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { Header() },
        content = { padding ->
            BodyContent(
                Modifier.padding(),
                specialItems = listOf(),
                regularItems = listOf(),
                screenHeight = 360.dp,
                screenWidth = 480.dp,
                navController = navController
            )
        },
        bottomBar = { Footer(modifier = Modifier, navController = navController) },
        snackbarHost = {}
    )
}

//@Preview(showBackground = true, widthDp= 360, heightDp = 640)

@SuppressLint("ObsoleteSdkInt")
@Composable
fun ShoppingMainScreen(navController: NavController) {

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

    Column(
        modifier = Modifier
            .width(screenWidth)
            .height(screenHeight)
    ) {
        Header(Modifier.weight(2.0f).background(color = MaterialTheme.colorScheme.background))
        MyDivider()
        //Spacer(modifier = Modifier.height(1.dp))
        Body(Modifier.weight(7f), screenHeight, screenWidth, navController = navController)
        MyDivider()
        Footer(Modifier.weight(1.0f), navController = navController)
    }


}


/*//@Preview
@Composable
fun PreviewHeader() {
    //com.example.portfolio.feature_shopping.presentation.main.Header(Modifier)
}*/

//***HEADER***
@SuppressLint("UnrememberedMutableState")
@Composable
fun Header(modifier: Modifier = Modifier) {
    val textState = remember { mutableStateOf(TextFieldValue("Search")) }
    Row(modifier = modifier) {
        Box() {
            Column(
                modifier = Modifier,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
            ) {

                Row(
                    modifier = Modifier
                    //.fillMaxWidth()
                    //.background(Color.White)
//                    ,//padding(top = 4.dp, bottom = 4.dp),
//                    horizontalArrangement = Arrangement.Center

                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        val (buttonCart, logoTitle, search) = createRefs()

                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .constrainAs(logoTitle) {
                                    absoluteLeft.linkTo(parent.start)
                                    absoluteRight.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(search.top)
                                },

                            text = "Find Beans",
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
                                painter = painterResource(drawable.coffee_icon_mugcup),
                                contentDescription = "Shopping Cart"
                            )
                        }

                        SearchView(
                            modifier = Modifier
                                .padding(top = 0.dp)
                                .constrainAs(search) {
                                    top.linkTo(logoTitle.bottom, margin = 4.dp)
                                    bottom.linkTo(parent.bottom, margin = 0.dp)
                                    absoluteLeft.linkTo(parent.absoluteLeft)
                                    absoluteRight.linkTo(parent.absoluteRight)
                                },
                            textState
                        )
                    }

/*            Image(
                modifier = Modifier
                    .clickable { }
                    .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                    //.clip(CircleShape)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .clip(RectangleShape),
                painter = painterResource(drawable.ic_face_48dp),
                contentDescription = "Logo",
            )
            */

                }

/*
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    com.example.portfolio.feature_shopping.presentation.main.SearchView(textState)
                }*/
/*
                val padding = 20.dp
                val density = LocalDensity.current
                Surface(
                    shape = RectangleShape,
                    color = Color.White,
                    tonalElevation = 12.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        //.padding(padding)
                        .drawWithContent {
                            val paddingPx = with(density) { padding.toPx() }
                            clipRect(
                                left = 0f,//-paddingPx,
                                top = 0f,
                                right = 0f,
                                bottom = size.height + paddingPx
                            ) {
                                this@drawWithContent.drawContent()
                            }
                        }
                ) {
                    Spacer(modifier.height(20.dp))
                    com.example.portfolio.feature_shopping.presentation.main.MyDivider()
                }

*/

            }
        }

    }


}

@Composable
fun MyDivider(modifier: Modifier = Modifier, height: Dp = 2.dp, shadowElevation: Dp = 8.dp) {
    Surface(
        shadowElevation = shadowElevation
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .background(color = com.example.portfolio.feature_shopping.presentation.ui.theme.Brown_700)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    modifier: Modifier,
    state: MutableState<TextFieldValue> = mutableStateOf<TextFieldValue>(TextFieldValue("Search"))
) {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var exp by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    androidx.compose.material3.ExposedDropdownMenuBox(
        modifier = modifier.background(color = Color.White),
        expanded = exp,
        onExpandedChange = { exp = !exp }) {
        TextField(
            modifier = Modifier.background(
                color = Color.White,//MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            ),
            value = selectedOption,
            onValueChange = { selectedOption = it },
            label = { Text("Search") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = exp)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        // filter options based on text field value (i.e. crude autocomplete)
        val filterOpts = options.filter { it.contains(selectedOption, ignoreCase = true) }
        if (filterOpts.isNotEmpty()) {
            ExposedDropdownMenu(expanded = exp, onDismissRequest = { exp = false }) {
                filterOpts.forEach { option ->
                    androidx.compose.material3.DropdownMenuItem(
                        onClick = {
                            selectedOption = option
                            exp = false
                        },
                        text = { Text(text = option) }
                    )
                }
            }
        }
    }


    /*TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },

        modifier = Modifier.fillMaxWidth()
            .clickable { }
            .background(color = ShoppingColors.DarkGrey, shape = RoundedCornerShape(4.dp)),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,

        //shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            //backgroundColor = ShoppingColors.LightColors.primary,//MaterialTheme.colors.primary,
            //focusedIndicatorColor = Color.Transparent,
            //unfocusedIndicatorColor = Color.Transparent,
            //disabledIndicatorColor = Color.Transparent
        ),
    )*/
}

@Composable
fun ItemList(state: MutableState<TextFieldValue>) {
    val items = remember() {
        mutableStateOf(listOf("Drink water", "Walk"))
    }
    var filteredItems: List<String>

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        val searchedText = state.value.text

        filteredItems = if (searchedText.isEmpty()) {
            items.value
        } else {
            val resultList = ArrayList<String>()
            for (item in items.value) {
                if (item.lowercase(Locale.getDefault())
                        .contains(searchedText.lowercase(Locale.getDefault()))
                ) {
                    resultList.add(item)
                }
            }
            resultList
        }
        items(filteredItems) { filteredItem ->

            ItemListItem(
                ItemText = filteredItem,
                onItemClick = { /*Click event code needs to be implement*/
                }
            )
        }

    }
}

@Composable
fun ItemListItem(ItemText: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onItemClick(ItemText) })
            .background(color = Color.Gray)//colorResource(id = R.color.purple_700))
            .height(57.dp)
            .fillMaxWidth()
            .padding(PaddingValues(8.dp, 16.dp))
    ) {
        Text(text = ItemText, fontSize = 18.sp, color = Color.White)
    }
}


//@Preview(widthDp = 360, heightDp = 640)
@Composable
fun BodyPreview(navController: NavController) {
    ShoppingTheme {
        Body(
            navController = navController
        )
    }
}

@Composable
fun registerVM(vm: ShoppingItemStateViewModel): Boolean {
    Log.d(TAG, "registerVM: registerVM is called")
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            launch {
                vm.onUIEvent(ShoppingUIEvent.AppLaunched)
            }
        }
    }
    return true
}

//---com.example.portfolio.feature_shopping.presentation.main.Body ----

@Composable
fun Body(
    modifier: Modifier = Modifier,
    screenHeight: Dp = 640.dp,
    screenWidth: Dp = 360.dp,
    vm: ShoppingItemStateViewModel = hiltViewModel(),//viewModel() //
    navController: NavController
) {
    val TAG = "MainScreen"

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val specialItems = vm.specialItemState.collectAsStateWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = Lifecycle.State.STARTED
    )

    /*by vm.specialItemState.collectAsStateWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = Lifecycle.State.STARTED
    )




    val regularItems: List<SellingItem> by vm.sellingItemState.collectAsStateWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = Lifecycle.State.STARTED
    )
 */
    val regularItems = vm.sellingItemState.collectAsStateWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = Lifecycle.State.STARTED
    )

//    Log.d(TAG, "Body: regular Item is ${regularItems.value.data?.get(0)?.imageUrl}")

    //com.example.portfolio.feature_shopping.presentation.main.SpecialSection(modifier)
    BodyContent(
        modifier,
        screenHeight = screenHeight,
        screenWidth = screenWidth,
        specialItems =
          when(specialItems.value){
               is Resource.Error -> emptyList()
               is Resource.Loading -> emptyList()
               is Resource.Success -> {
                   specialItems.value.data!!
               }
           },
        regularItems =
            when(regularItems.value){
                is Resource.Error -> emptyList()
                is Resource.Loading -> {
                    Log.d(TAG, "Body: Loading")
                    emptyList()
                }
                is Resource.Success -> {
                    Log.d(TAG, "Body: Success ${regularItems.value.data?.get(0)}")
                    regularItems.value.data!!
                }
            },
        navController =  navController
    )
    //com.example.portfolio.feature_shopping.presentation.main.GridView(modifier)
}

private fun onItemClicked(clickedItem:SellingItem){


}


/**
 * Ref: https://stackoverflow.com/questions/70590404/jetpack-compose-lazyverticalgrid-items-throws-java-lang-illegalstateexception
 * Any extra composables must be added outside the items block,
 * or as extra items block inside the LazyVerticalGrid()
 * will cause Error : Place was called on a node which was placed already
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun BodyContent(
    modifier: Modifier = Modifier,
    specialItems: List<SpecialItem>,
    regularItems: List<SellingItem>,
    screenHeight: Dp = 360.dp,
    screenWidth: Dp = 640.dp,
    navController: NavController,
    vm:ShoppingItemStateViewModel = hiltViewModel()
) {

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells
            .Fixed(2),

        // content padding
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        //Span mean it how many spot will it take.
        //since it is fixed with 2 counts == 2 spans.

        // First Section with title and LazyRow
        // It takes two span to override
        item(
            span = { GridItemSpan(2) }
        ) {
            /*LazyRow {
                items(list2){ item ->
                    Card(
                        backgroundColor = Color.Red,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        elevation = 8.dp,
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(8.dp)       ,
                            contentScale = ContentScale.FillBounds,
                            painter = item.image, //list[0].image,
                            contentDescription = "null"
                        )
                        Text(
                            text = item.description,//list[0].description,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            }*/
            SpecialSection(
                modifier = Modifier
                    .width(360.dp)
                    .height(400.dp),
                specialItems = specialItems
            )
        }

        //Section Item
        //Start with title
        item(
            span = { GridItemSpan(2) }
        ) {
            Text(
                text = "Regular Items",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Third Section with grid items
        // Grid To Display
        itemsIndexed(
            items = regularItems,
            key = { index, item ->
                item.id
            }
            /*.filter{
            it.type!=SellingItem.DisplayType.SPECIAL
        }*/

        ) { index, item ->

            EachItemTwo(
                modifier = Modifier
                    .width(
                        //Rotated
                        if (screenWidth > screenHeight) screenWidth.times(0.27f) else screenWidth.times(
                            0.48f
                        )
                    )
                    //.fillMaxWidth(0.45f)
                    .height(
                        if (screenWidth > screenHeight) screenHeight.times(0.48f) else screenHeight.times(
                            0.27f
                        )

                    ).clickable {
                        navController.navigate(route = "detail_screen/" + index)
                    },
                painter = if (item.imageUrl != "") rememberAsyncImagePainter(item.imageUrl)
                //Image for local use for design
                else painterResource(item.image),
                text = item.description
            )


        }
    }
}

@Composable
fun GridView(
    modifier: Modifier = Modifier,
    list: List<SellingItem> = listOf(
        SellingItem(
            0,
            (R.drawable.coffee_animation),
            description = "1",
            title = "One",
            price = 1.99,
            quantity = 10,
        ),
        SellingItem(
            1,
            (R.drawable.coffee_animation),
            description = "2",
            title = "Two",
            price = 2.99,
            quantity = 10,
        ),
        SellingItem(
            2,
            (R.drawable.coffee_animation),
            description = "3",
            title = "Three",
            price = 3.99,
            quantity = 10,
        ),
        SellingItem(
            3,
            (R.drawable.coffee_animation),
            description = "4",
            title = "Four",
            price = 4.99,
            quantity = 10,
        ),
        SellingItem(
            4,
            (R.drawable.coffee_animation),
            description = "5",
            title = "Five",
            price = 5.99,
            quantity = 10,
        ),
        SellingItem(
            5,
            (R.drawable.coffee_animation),
            description = "6",
            title = "Six",
            price = 6.99,
            quantity = 10,
        ),
        SellingItem(
            6,
            (R.drawable.coffee_animation),
            description = "7",
            title = "Seven",
            price = 7.99,
            quantity = 10,
        ),
    )
) {
    val mlist = list

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),//.Adaptive(minSize = 128.dp),
        content = {
            items(mlist.size) { item ->
                EachItem(
                    modifier = Modifier.wrapContentWidth(),
                    painter = painterResource(list[item].image),
                    text = list[item].description
                )
                /* when{
                     item.type == SellingItem.DisplayType.SINGLE -> {
                         Column(modifier = modifier
                             .fillMaxWidth()
                             .height(128.dp)
                         ){
                             Text("Single Item Display")
                             com.example.portfolio.feature_shopping.presentation.main.EachItem(
                                 modifier = Modifier.fillMaxWidth(),
                                 painter = item.image,
                                 //contentDescription = item.description,
                                 text = item.description
                             )
                         }
                     }
                     item.type == SellingItem.DisplayType.MULTIPLE -> {
                         com.example.portfolio.feature_shopping.presentation.main.EachItem(
                             modifier = Modifier.wrapContentWidth(),
                             painter = item.image,
                             text= item.description
                         )
                     }
                 }*/
            }
        }
    )

}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun SpecialSection(
    modifier: Modifier = Modifier.size(200.dp),
    specialItems: List<SpecialItem> = listOf(
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
) {

    ConstraintLayout(
        modifier = modifier
    ) {
        val (titleString, content, spacer) = createRefs()
        Box(
            modifier = Modifier
                //.padding(start =0.dp, bottom = 8.dp)
                .constrainAs(titleString) {
                    top.linkTo(parent.top, margin = 8.dp)
                },
        ) {
            Text(
                text = "Special Section",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Box(
            modifier = Modifier
                //.background(color=Color.Blue)
                //.padding(top= 8.dp, bottom = 8.dp)
                .constrainAs(content) {
                    top.linkTo(titleString.bottom, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                    absoluteLeft.linkTo(parent.absoluteLeft, margin = 8.dp)
                    absoluteRight.linkTo(parent.absoluteRight, margin = 8.dp)
                }
        ) {
            LazyRow(
                modifier = Modifier
                //.fillMaxHeight()
                //.height(100.dp),
            ) {
                items(
                    items = specialItems,
                    key = { each ->
                        each.id
                    }
                ) { item ->
                    EachItem(
                        modifier = Modifier
                            .fillParentMaxWidth(1f)
                            .fillParentMaxHeight(0.8f),
                        painter =
                        //Image data from Server
                        if (item.imageUrl != "") rememberAsyncImagePainter(item.imageUrl)
                        //Image for local use for design
                        else painterResource(item.image),
                        contentDescription = item.description,
                        text = item.description
                    )
                }
            }
        }
    }
}

@Composable
fun EachItem(
    modifier: Modifier = Modifier.size(128.dp),
    painter: Painter = painterResource(R.drawable.coffee_animation),
    contentDescription: String = "null",
    text: String = "Title of Item",
    screenHeight: Dp = 400.dp,
    screenWidth: Dp = 360.dp
) {
    val radius = 10.dp

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(radius),
        elevation = 8.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(8.dp)
                    //.fillMaxWidth()
                    //.fillMaxSize()

                    .weight(8f),
                contentScale = ContentScale.FillBounds,
                painter = painter,
                contentDescription = contentDescription
            )
            Text(
                text = text,
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .background(ShoppingColors.LightColors.primary)
            )
        }
    }


}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EachItemTwo(
    modifier: Modifier = Modifier.size(128.dp),
    painter: Painter = painterResource(R.drawable.coffee_animation),
    contentDescription: String = "null",
    text: String = "Title of Item",
    screenHeight: Dp = 400.dp,
    screenWidth: Dp = 360.dp,
    vm:ShoppingItemStateViewModel = hiltViewModel()
) {
    val radius = 10.dp

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(radius),
        elevation = 8.dp
    ) {
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
                        },
                    //.fillMaxWidth()
                    //.fillMaxSize()

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
                            //https://stackoverflow.com/questions/64171607/how-to-use-bias-in-constraint-layout-compose
                            // linkTo(bias = 0f)
                        },
                )
            }
        }

    }


}

//***com.example.portfolio.feature_shopping.presentation.main.Footer***
@Composable
fun Footer(
    modifier: Modifier,
    counter: Int = 1,
    vm: ShoppingListStateViewModel = hiltViewModel(),//viewModel()//,
    navController: NavController
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 50.dp)
            //.fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onTertiary,
                shape = RoundedCornerShape(2.dp)
            )
            .clip(shape = RoundedCornerShape(2.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
/*        Box(
            modifier = Modifier
                .weight(3f)
                .clickable { },
            contentAlignment = Alignment.Center,

            ) {
            IconButton(
                onClick = {}
            ){
                androidx.compose.material3.Icon(
                    painter = painterResource(R.drawable.coffee_menu_btn),
                    contentDescription = "Coffee Menu Button"
                )
            }
        }*/

        fastImageButton(
            modifier = Modifier.weight(3f),
            onClick = {
                navController
                println("First Image Clicked TO THE PROFILE")
            },
            painter = painterResource(drawable.coffee_menu_btn)
        )

        fastImageButton(
            modifier = Modifier.weight(3f),
            onClick = {
                navController.navigate(Screen.Main.rout) {
                    popUpTo(Screen.Main.rout)
                }
                println("First Image Clicked TO THE HOME")

            },
            painter = painterResource(drawable.coffee_home_btn)
        )

        fastImageButton(
            modifier = Modifier.weight(3f).defaultMinSize(50.dp),
            onClick = {
                navController.navigate(Screen.Cart.rout) {
                    popUpTo(Screen.Cart.rout)
                }
                println("First Image Clicked TO THE CART!!")

            },
            painter = painterResource(drawable.coffee_cart_btn),
        ) {
            if (vm.counter.value >= 1) {
                Text(
                    text = " ${vm.counter.value.toString()} ",
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    ),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun fastImageButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    painter: Painter,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clickable { },
        contentAlignment = Alignment.Center,

        ) {
        ConstraintLayout(modifier = Modifier.padding(top = 18.dp)) {

            val (IconBtn, Counter) = createRefs()

            Box(modifier = Modifier
                .constrainAs(Counter) {
                    top.linkTo(parent.top)
                    bottom.linkTo(IconBtn.top)
                    absoluteRight.linkTo(parent.absoluteRight)
                    //absoluteLeft.linkTo(IconBtn.absoluteRight)


                    //linkTo( IconBtn.absoluteLeft, parent.absoluteRight, startMargin = 8.dp, bias = 1f)
                    width = Dimension.fillToConstraints
                }
                //.background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape),

            ) {
                content()
            }

            IconButton(
                modifier = Modifier.constrainAs(IconBtn) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
                onClick = { onClick() }
            ) {
                androidx.compose.material3.Icon(
                    painter = painter,
                    contentDescription = "Coffee Menu Button"
                )
            }


        }

    }
}
