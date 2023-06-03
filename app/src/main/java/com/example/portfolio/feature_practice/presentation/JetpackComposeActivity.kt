package com.example.portfolio.feature_practice.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.portfolio.R
import com.example.portfolio.feature_practice.presentation.ui.theme.JetpackComposeTheme
import com.example.portfolio.feature_shopping.presentation.ui.theme.ShoppingTheme
import java.util.*

class JetpackComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                //JetpackCompose_BasicCodeLab()
                //JetpackCompose_Layout()
                JetpackCompose_State()
            }
        }
    }
}

@Preview
@Composable
fun `PreviewJetpackCompose_State`(){
    JetpackComposeTheme {
        JetpackCompose_State()

    }
}

@Composable
fun JetpackCompose_State(modifier:Modifier = Modifier){

}




/**
 * Below is Compose Layout
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun `JetpackCompose_Layout` (modifier:Modifier = Modifier){
    JetpackComposeTheme {
        //SearchBar(Modifier.padding(8.dp))
        //AlignYourBodyElement()
        //FavoriteCollectionCard(modifier = Modifier.padding(8.dp))
        //HomeSection(title = "Align Your com.example.portfolio.feature_shopping.presentation.main.Body", content = { AlignYourBodyRow() })
//        HomeScreen()
        MyApp()
    }
}
/*

@Preview
@Composable
fun LayoutPreview(){
    JetpackComposeTheme {
        SearchBar(Modifier.padding(8.dp))
    }
}

*/

/*@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, widthDp = 360, heightDp = 600)
@Composable
fun PreviewAlignElement(){
    JetpackComposeTheme {
        //AlignYourBodyElement(Modifier.padding(8.dp))
        //FavoriteCollectionCard(modifier = Modifier.padding(8.dp))
        //AlignYourBodyRow()
        //CollectionGrid(Modifier.padding(8.dp))
*//*        HomeSection(title = "Align Your com.example.portfolio.feature_shopping.presentation.main.Body"){
            HomeScreen()
        }*//*

        MyApp()
    }
    
}*/

@Composable
fun HomeSection(
    modifier:Modifier = Modifier,
    title:String,
    content: @Composable () ->Unit
){
    Column(
        modifier=modifier
    ){
        Text(
            title.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .paddingFromBaseline(top=40.dp, bottom = 8.dp)
                .padding(horizontal = 16.dp)
        )

        content()

    }

}

@Composable
fun HomeScreen(modifier: Modifier=Modifier){
    Column(
        modifier
        //.padding(vertical = 16.dp)
        .verticalScroll(rememberScrollState())
    ){
        Spacer(Modifier.height(16.dp))
        SearchBar(Modifier.padding(horizontal = 16.dp))

        HomeSection(title= "Align Your com.example.portfolio.feature_shopping.presentation.main.Body", content = { AlignYourBodyRow()})
        HomeSection(title="Favorite Collections"){
            CollectionGrid()
        }
        //CustomedBottomNavigation()
    }
}

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(modifier:Modifier = Modifier){
    Scaffold(
        bottomBar = {CustomedBottomNavigation()}
    ){ padding ->
        HomeScreen(Modifier.padding(padding))
    }
}
@Composable
fun CustomedBottomNavigation(modifier:Modifier = Modifier){
    BottomNavigation(modifier){
        BottomNavigationItem(
            selected=false,
            onClick = {},
            icon = {Icon(Icons.Default.Spa, contentDescription = null)},
            enabled = false,
            label = {Text("Home")},
            alwaysShowLabel = false,)

        BottomNavigationItem(
            selected=false,
            onClick = {},
            icon = {Icon(Icons.Default.AccountBox, contentDescription = null)},
            enabled = false,
            label = {Text("Profile")},
            alwaysShowLabel = false,)
    }
}


@Composable
fun CollectionGrid(modifier:Modifier = Modifier){
    var count by remember{mutableStateOf(listOf(1,2,3,4,5,6,7,8,9,10,11))}
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.height(120.dp)
    ) {
        items(count){ item ->
            FavoriteCollectionCard(text = item.toString())
        }
    }
}

@Composable
fun AlignYourBodyRow(
    modifier:Modifier = Modifier
){
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(10){ item->
            AlignYourBodyElement()
        }
    }
}

@Composable
fun FavoriteCollectionCard(
    @DrawableRes drawable:Int = R.drawable.coffee_steam_,
    text:String="tester",
    //@StringRes  text:Int = R.string.app_name,
    modifier:Modifier = Modifier
){
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface
    ){
        Row(
            modifier= Modifier
                .width(192.dp)
                .height(56.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,

        ){
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier= Modifier
                    .width(92.dp)
                    .height(56.dp)
                    .weight(0.4f),

            )
            Text(
                text= text,//stringResource(text),
                modifier = Modifier.padding(horizontal = 16.dp)
                    .weight(0.6f)
            )
        }
    }

}

@Composable
fun AlignYourBodyElement (modifier:Modifier = Modifier){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        Image(
            painter = painterResource(R.drawable.coffee_bean_falling),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(88.dp).clip(shape = CircleShape)
        )
        Text(
            text = stringResource(R.string.compose_layout_alignbody_text),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.paddingFromBaseline(top = 24.dp,bottom= 8.dp)

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier:Modifier = Modifier){
    TextField(
        value ="",
        onValueChange = {},
        modifier = modifier
            .heightIn(min=56.dp)
            .fillMaxWidth(),
        leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = stringResource(R.string.compose_layout_searchbar
            ))
        },
        placeholder = {
            Text(stringResource(R.string.compose_layout_searchbar_placeholder))
        },
        colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.surface
        )
    )
}








/**
 *
 * Below is for Basic CodeLab
*/

/*
@Preview(
    showBackground = true,
    widthDp =320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name= "Dark",
    showSystemUi = true,
)
@Preview (
    showSystemUi = true,
    showBackground = true,
    widthDp=320,
)
@Composable
fun CoursePreView(){
    JetpackComposeCourse()
}
*/


@Composable
fun JetpackCompose_BasicCodeLab(modifier: Modifier = Modifier){
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier.fillMaxWidth()
        //.background(color = MaterialTheme.colorScheme.secondary),
    ){
        if(shouldShowOnboarding){
            OnboardingScreen(onContinueClicked = {
                shouldShowOnboarding = false
                println("JetpackComposeCourse: shouldShowOnBoarding = $shouldShowOnboarding")
            })
        }else{
            Greetings()
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(100){"$it"},
) {

    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {

        items(items = names){ name ->
            Greeting(name = name)
        }

/*        for (name in names) {
            Greeting(name = name)
        }*/
    }
}




@Composable
private fun Greeting(name: String) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
    }
}


@Composable
private fun CardContent(name:String){
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
            )

    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(12.dp)
            //.padding(bottom = extraPadding.coerceAtLeast(0.dp))
        ) {
            Text(text = "Hello, ")
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )

            if(expanded) Text(stringResource(R.string.compose_random_strings)) else Unit
        }

        IconButton(
            onClick = { expanded = !expanded},
        ){
            Icon(
                imageVector = if(expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore ,
                contentDescription = if(expanded) stringResource(R.string.show_less) else stringResource(
                    R.string.show_more)
            )
        }
/*            ElevatedButton(
                onClick = { expanded.value = !expanded.value }

            ) {
                Text(if (expanded.value) "Show less" else "Show more")

            }*/
    }
}





@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onContinueClicked:()-> Unit
){

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ){
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical =  24.dp),
            onClick = onContinueClicked
        ){
            Text(
                text = "Continue",
                color = Color.White
            )
        }
    }
}


@Composable
fun Greeting2(name:String = "Ray", modifier:Modifier = Modifier) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val extraPadding = if(expanded.value) 48.dp else 0.dp

    Row(modifier = Modifier
        .padding(24.dp)
        .background(color = MaterialTheme.colorScheme.tertiary)
        .height(150.dp),
    ) {
        Column(modifier = modifier
            .weight(0.4f)
            .padding(bottom = extraPadding)

        ){
            Text(
                modifier = Modifier.padding(14.dp),
                text =  "Hello"
            )
            Text(
                modifier = Modifier.padding(14.dp),
                text =  "$name"
            )
        }
        ElevatedButton(
            onClick =  {expanded.value = !expanded.value}
        ){
            Text(if(expanded.value) "Show Less" else "Show More!")
        }


    }

}

//@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview(){
    ShoppingTheme {
        Surface(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal =  8.dp)

        ){
            Greeting2()
        }
    }
}



