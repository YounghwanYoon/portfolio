package com.example.portfolio.feature_shopping.presentation.search

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.portfolio.feature_shopping.presentation.search.util.SearchBarState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navController:NavController,
    searchVM:SearchViewModel
){
    val searchBarState by searchVM.searchBarState
    val searchTextState by searchVM.searchTextState.collectAsState()
    val isSearching by searchVM.isSearhing.collectAsState()
    val matchedItems by searchVM.matchedItems.collectAsState()

    Scaffold(
       topBar = {
            CustomTopBar(
                searchBarState = searchBarState,
                searchTextState = searchTextState,
                onSearchTriggered = {
                    //when search icon is clicked
                    searchVM.updateSearchState(updatedValue = SearchBarState.OPENED)
                },
                onTextChange = searchVM::updateSearchTextState, //{itemStateVM.updateSearchTextState(updatedValue= it)}
                onCloseClicked = {
                    searchVM.updateSearchTextState(updatedValue = "")
                    searchVM.updateSearchState(updatedValue = SearchBarState.CLOSED)
                },
                onSearchClicked = {
                    println("Searched Text $it")
                },
            )
       },
    ){
        //for content, show different item base on isSearching state
        //need to display items on body.
        when(isSearching){
            true -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            false ->{
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    items(matchedItems){items ->
                        //
                        Text(
                            text = items.title,
                            color = Color.Black
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun BouncingBall() {
    val radius = 50f

    val animatableX = remember { Animatable(0f) }
    val animatableY = remember { Animatable(0f) }

    val configuration = LocalConfiguration.current

    val screenHeight: Dp
    val screenWidth: Float

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
        screenHeight = 400.dp
        screenWidth = 360.dp.value
    } else {
        screenHeight = configuration.screenHeightDp.dp
        screenWidth = configuration.screenWidthDp.dp.value - radius *2
    }
    //val screenWidth = LocalConfiguration.current.dimension.screenWidthDp.dp.value - radius * 2

    LaunchedEffect(Unit) {
        animatableX.animateTo(
            targetValue = screenWidth,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        )
        animatableY.animateTo(
            targetValue = screenWidth,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        )
    }

    val x by animateFloatAsState(targetValue = animatableX.value)
    val y by animateFloatAsState(targetValue = animatableY.value)

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.Gray,
            radius = center.x + x,
            center = Offset(0f, (center.y + y)),//.intOffset,
            radius,
        )
    }
}

@Composable
fun CustomTopBar(
    searchBarState: SearchBarState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit
){

    SearchView(
        searchTextState = searchTextState,
        onTextChange = onTextChange,
        onCloseClicked = onCloseClicked,
        onClicked = onSearchClicked
    )

/*
    when(searchBarState){
        SearchBarState.CLOSED -> {
            DefaultSearchView(
                onSearchClicked = onSearchTriggered
            )
        }

        SearchBarState.OPENED -> {
            SearchView(
                searchTextState = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onClicked = onSearchClicked
            )
        }
    }
*/
}


@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    searchTextState:String = "",
    onTextChange: (String) -> Unit,
    onCloseClicked: ()->Unit,
    onClicked:(String) -> Unit,
) {
    var selectedOption by remember { mutableStateOf("") }
    var text by remember{ mutableStateOf(searchTextState) }

    Surface(modifier =modifier){
        TextField(
            modifier = Modifier.background(
                color = Color.Transparent,//MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            ),
            value = searchTextState,//selectedOption,
            onValueChange = {
                selectedOption = it
                onTextChange(it)
            },
            placeholder = { Text(text = "Search Bean...", color = MaterialTheme.colorScheme.onBackground) },
            singleLine = true,
            //label = { Text("Search") },
            leadingIcon = {
                IconButton (
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    onClick = {}
                ){
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            trailingIcon = {
                IconButton (
                    onClick = {
                        if(text.isNotEmpty()){
                            onTextChange("")
                        }else{
                            onCloseClicked()
                        }
                    }
                ){
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                //ExposedDropdownMenuDefaults.TrailingIcon(expanded = exp)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            )
        )
    }
}

@Composable
fun DefaultSearchView(
    modifier: Modifier = Modifier,
    onSearchClicked:() -> Unit = {},
    text:String = "Search Your Bean..."
){
    Surface(modifier =modifier){
        TextField(
            modifier = Modifier.background(
                color = Color.Transparent,//MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            ),
            value = "",
            onValueChange = {
            },
            placeholder = { Text(text = "Search Bean...", color = MaterialTheme.colorScheme.onBackground) },
            singleLine = true,
            //label = { Text("Search") },

            trailingIcon = {
                IconButton (
                    onClick = onSearchClicked
                ){
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }

                //ExposedDropdownMenuDefaults.TrailingIcon(expanded = exp)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)

            ) //ExposedDropdownMenuDefaults.textFieldColors(),
        )

/*        androidx.compose.material3.ExposedDropdownMenuBox(
            //modifier = modifier.background(color = Color.White),
            expanded = exp,
            onExpandedChange = { exp = !exp }
        ) {

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
        }*/
    }
}