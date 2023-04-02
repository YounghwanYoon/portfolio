package com.example.portfolio.feature_shopping.presentation.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.presentation.search.util.SearchBarState
import com.example.portfolio.feature_shopping.presentation.utils.Screens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navController:NavController,
    onBackClicked: ()->Unit = {},
    searchVM:SearchViewModel,
    isTablet:Boolean
){
    val searchBarState by searchVM.searchBarState
    val searchTextState by searchVM.searchTextState.collectAsState()
    val isSearching by searchVM.isSearching.collectAsState()
    val matchedItems by searchVM.matchedItems.collectAsState()
    println("isSearching $isSearching")
    println("searchTextState $searchTextState")
    println("size of matchedItems ${matchedItems.size}")

    navController.previousBackStackEntry != null
    Scaffold(
       topBar = {
           Row(
               verticalAlignment = Alignment.Top,
               horizontalArrangement = Arrangement.Center
           ){
               Box(contentAlignment = Alignment.CenterStart){
                   IconButton(
                       onClick = {navController.navigate(Screens.Main.rout)},
                   ){
                       Icon(
                           imageVector = Icons.Filled.ArrowBack,
                           contentDescription = "Back To Previous Screen",
                       )
                   }
               }
               Box(
                   contentAlignment = Alignment.Center
               ){
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
               }
           }
       },
    ){
        //for content, show different item base on isSearching state
        //need to display items on body.
        Spacer(modifier = Modifier.height(4.dp))
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
                    items(matchedItems){item ->
                        //
                        TextButton(
                            onClick = {
                                println("clicked ${item.title}")
                                navController.navigate(
                                    route = Screens.Detail.withArgs("${item.id}")
                                )
                            }
                        ){
                            Text(
                                text = item.title,
                                color = Color.Black
                            )
                        }

                    }
                }
            }
        }

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
    val text by remember{ mutableStateOf(searchTextState) }

    Surface(modifier =modifier){
        Card(
            shape = RoundedCornerShape(4.dp),
            backgroundColor = Color.White
        ){
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
                placeholder = {
                    Text(
                        text = stringResource(R.string.searchview_text),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                  },
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