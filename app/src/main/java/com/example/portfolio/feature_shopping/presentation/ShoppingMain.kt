package com.example.portfolio.feature_shopping.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

import com.example.portfolio.R
import com.example.portfolio.R.*
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.presentation.ui.theme.ShoppingTheme
import com.example.portfolio.feature_shopping.presentation.utils.ShoppingColors
import java.util.*
import kotlin.collections.ArrayList

class ShoppingDemo : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingTheme {
                //ShoppingApp()
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

                DetailScreen(screenHeight = screenHeight, screenWidth = screenWidth, window = window)
            }
        }
/*        ComposeView(this).apply {
            setContent {
                ShoppingApp()
            }
        }*/
    }
}




//@Preview(showBackground = true, widthDp= 360, heightDp = 640)
@Composable
fun PreviewShoppingApp(){
    ShoppingTheme {
        ShoppingScreen(Modifier.background(
            color = androidx.compose.material3.MaterialTheme.colorScheme.background
        ))
    }
}



@Composable
fun ShoppingApp(){

    ShoppingScreen(Modifier.background(
        color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ))

}

@SuppressLint("ObsoleteSdkInt")
@Composable
fun ShoppingScreen(modifier:Modifier =Modifier) {

    val configuration = LocalConfiguration.current
    val screenHeight:Dp
    val screenWidth:Dp

    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2 ){
        screenHeight = 400.dp
        screenWidth  = 360.dp
    }else {
        screenHeight = configuration.screenHeightDp.dp
        screenWidth  = configuration.screenWidthDp.dp
    }

    Column(
        modifier = modifier
            .width(screenWidth)
            .height(screenHeight)
    ) {
        Header(Modifier.weight(2.0f).background(color = MaterialTheme.colorScheme.background))
        MyDivider()
        //Spacer(modifier = Modifier.height(1.dp))
        Body(Modifier.weight(7f), screenHeight, screenWidth)
        MyDivider()
        Footer(Modifier.weight(1.0f))
    }


}


/*//@Preview
@Composable
fun PreviewHeader() {
    //Header(Modifier)
}*/

//***HEADER***
@SuppressLint("UnrememberedMutableState")
@Composable
fun Header(modifier: Modifier) {
    val textState = remember { mutableStateOf(TextFieldValue("Search")) }
    Row(modifier = modifier){
        Box(){
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

                    ){
                        val (buttonCart, logoTitle, search) = createRefs()

                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .constrainAs(logoTitle){
                                    absoluteLeft.linkTo(parent.start)
                                    absoluteRight.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(search.top)
                                }
                            ,

                            text = "Find Beans",
                            style = TextStyle(
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onTertiary,
                                fontSize = 24.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        androidx.compose.material3.IconButton(
                            modifier = Modifier.constrainAs(buttonCart){
                                top.linkTo(logoTitle.top)
                                bottom.linkTo(logoTitle.bottom)
                                //absoluteLeft.linkTo(logoTitle.absoluteRight, margin = 8.dp)
                                absoluteRight.linkTo(parent.end, margin = 8.dp)
                            },
                            onClick = {}
                        ){
                            androidx.compose.material3.Icon(
                                modifier = Modifier,
                                painter = painterResource(drawable.coffee_icon_mugcup),
                                contentDescription = "Shopping Cart"
                            )
                        }

                        SearchView(
                            modifier= Modifier
                                .padding(top = 0.dp)
                                .constrainAs(search){
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
                    SearchView(textState)
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
                    MyDivider()
                }

*/


            }
        }

    }


}

@Composable
fun MyDivider(modifier:Modifier = Modifier, height: Dp = 2.dp, shadowElevation:Dp = 8.dp){
    Surface(
        shadowElevation = shadowElevation
    ){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .background(color= com.example.portfolio.feature_shopping.presentation.ui.theme.Brown_700)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(modifier: Modifier, state: MutableState<TextFieldValue> = mutableStateOf<TextFieldValue>(TextFieldValue("Search"))) {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var exp by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    androidx.compose.material3.ExposedDropdownMenuBox(modifier = modifier.background(color = Color.White), expanded = exp, onExpandedChange = { exp = !exp }) {
        TextField(
            modifier = Modifier.background(
                    color = Color.White,//MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(24.dp)
                ),
            value = selectedOption,
            onValueChange = { selectedOption = it },
            label = { androidx.compose.material3.Text("Search") },
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
                        text = {androidx.compose.material3.Text(text=option)}
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
        androidx.compose.material3.Text(text = ItemText, fontSize = 18.sp, color = Color.White)
    }
}



//@Preview(widthDp = 360, heightDp = 640)
@Composable
fun BodyPreview(){
    ShoppingTheme{
        Body()
    }
}


//---Body ----
@Composable
fun Body(modifier: Modifier = Modifier, screenHeight:Dp = 640.dp, screenWidth:Dp = 360.dp) {
    //SpecialSection(modifier)
    BodyContent(modifier, screenHeight = screenHeight, screenWidth = screenWidth)
    //GridView(modifier)
}


/**
 * Ref: https://stackoverflow.com/questions/70590404/jetpack-compose-lazyverticalgrid-items-throws-java-lang-illegalstateexception
 * Any extra composables must be added outside the items block,
 * or as extra items block inside the LazyVerticalGrid()
 * will cause Error : Place was called on a node which was placed already
 */
@Composable
fun BodyContent(
    modifier: Modifier = Modifier,
    specialList:List<SellingItem> = listOf(
        SellingItem(0, painterResource(R.drawable.coffee_animation), description = "one", type = SellingItem.DisplayType.SPECIAL),
        SellingItem(1, painterResource(R.drawable.coffee_animation), description = "two"),
        SellingItem(2, painterResource(R.drawable.coffee_animation), description = "three"),
        SellingItem(3, painterResource(R.drawable.coffee_animation), description = "four"),
    ),
    list:List<SellingItem> =listOf(
            SellingItem(0, painterResource(R.drawable.coffee_animation), description = "one", type = SellingItem.DisplayType.SPECIAL),
            SellingItem(1, painterResource(R.drawable.coffee_animation), description = "two"),
            SellingItem(2, painterResource(R.drawable.coffee_animation), description = "three"),
            SellingItem(3, painterResource(R.drawable.coffee_animation), description = "four"),
            SellingItem(4, painterResource(R.drawable.coffee_animation), description = "five"),
            SellingItem(5, painterResource(R.drawable.coffee_animation), description = "six"),
            SellingItem(6, painterResource(R.drawable.coffee_animation), description = "seven"),
            SellingItem(7, painterResource(R.drawable.coffee_animation), description = "8"),
            SellingItem(8, painterResource(R.drawable.coffee_animation), description = "9"),
            SellingItem(9, painterResource(R.drawable.coffee_animation), description = "10"),
            SellingItem(10, painterResource(R.drawable.coffee_animation), description = "11"),
            SellingItem(11, painterResource(R.drawable.coffee_animation), description = "12"),
        ),
    screenHeight:Dp,
    screenWidth: Dp
){

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

    ){
        //Span mean it how many spot will it take.
        //since it is fixed with 2 counts == 2 spans.

        // First Section with title and LazyRow
        // It takes two span to override
        item (
            span = {GridItemSpan(2)}
        ){
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
                list = specialList
            )
        }

        //Section Item
        //Start with title
        item(
            span ={GridItemSpan(2)}
        ){
            Text(
                text = "Regular Items",
                style = MaterialTheme.typography.bodySmall,
                modifier= Modifier.padding(bottom = 8.dp)
            )
        }

        // Third Section with grid items
        // Grid To Display
        itemsIndexed(
            items= list,
            key = {index, item ->
                item.id
            }
            /*.filter{
            it.type!=SellingItem.DisplayType.SPECIAL
        }*/

        ) { index, item ->
            EachItemTwo(
                modifier= Modifier
                    .width(
                        //Rotated
                        if(screenWidth > screenHeight) screenWidth.times(0.27f) else screenWidth.times(0.48f)
                    )
                    //.fillMaxWidth(0.45f)
                    .height(
                        if(screenWidth > screenHeight) screenHeight.times(0.48f) else screenHeight.times(0.27f)

                    ),
                painter = item.image,
                text = item.description
            )
            /*this@LazyVerticalGrid.item (
                //span = {GridItemSpan(2)}
            ){
                EachItemTwo(
                    modifier= Modifier.fillMaxWidth(0.45f),
                    painter = item.image,
                    text = item.description
                )
*//*                Card(
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
                        painter = list[index].image,
                        contentDescription = "null"
                    )
                    Text(
                        text = list[index].description,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }*//*
            }*/
        }
    }

}

@Composable
fun GridView(
    modifier: Modifier = Modifier,
    list:List<SellingItem> = listOf(
        SellingItem(0, painterResource(R.drawable.coffee_animation), description = "one", type = SellingItem.DisplayType.SPECIAL),
        SellingItem(1, painterResource(R.drawable.coffee_animation), description = "two"),
        SellingItem(2, painterResource(R.drawable.coffee_animation), description = "three"),
        SellingItem(3, painterResource(R.drawable.coffee_animation), description = "four"),
        SellingItem(4, painterResource(R.drawable.coffee_animation), description = "five"),
        SellingItem(5, painterResource(R.drawable.coffee_animation), description = "six"),
        SellingItem(6, painterResource(R.drawable.coffee_animation), description = "seven"),
    )
){
    val mlist = list

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),//.Adaptive(minSize = 128.dp),
        content = {
            items(mlist.size){ item ->
                EachItem(
                    modifier = Modifier.wrapContentWidth(),
                    painter = list[item].image,
                    text= list[item].description
                )
                /* when{
                     item.type == SellingItem.DisplayType.SINGLE -> {
                         Column(modifier = modifier
                             .fillMaxWidth()
                             .height(128.dp)
                         ){
                             Text("Single Item Display")
                             EachItem(
                                 modifier = Modifier.fillMaxWidth(),
                                 painter = item.image,
                                 //contentDescription = item.description,
                                 text = item.description
                             )
                         }
                     }
                     item.type == SellingItem.DisplayType.MULTIPLE -> {
                         EachItem(
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


@Composable
fun SpecialSection(
    modifier:Modifier = Modifier.size(200.dp),
    list:List<SellingItem> = listOf(
        SellingItem(0, painterResource(R.drawable.coffee_steam_), description = "one", type = SellingItem.DisplayType.SPECIAL),
        SellingItem(1, painterResource(R.drawable.coffee_steam_), description = "two", type = SellingItem.DisplayType.SPECIAL),
        SellingItem(3, painterResource(R.drawable.coffee_steam_), description = "tree", type = SellingItem.DisplayType.SPECIAL),
    ),
){
    var listState = rememberLazyGridState()

    ConstraintLayout(
        modifier = modifier
    ) {
        val (titleString, content, spacer)  = createRefs()
        Box(
            modifier = Modifier
                .padding(start = 8.dp, bottom = 8.dp)
                .constrainAs(titleString){
                    top.linkTo(parent.top, margin = 8.dp)
                },
        ){
            Text(
                text = "Special Section",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Box(
            modifier = Modifier
                //.background(color=Color.Blue)
                .padding(top= 8.dp, bottom = 8.dp)
                .constrainAs(content){
                    top.linkTo(titleString.bottom, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                    absoluteLeft.linkTo(parent.absoluteLeft,margin =8.dp)
                    absoluteRight.linkTo(parent.absoluteRight, margin = 8.dp)
                }
        ) {
            LazyRow(
                modifier = Modifier
                    //.fillMaxHeight()
                    //.height(100.dp),
            ) {
                items(
                    items = list,
                    key = { each ->
                        each.id
                    }
                ){ item ->
                    EachItem(
                        modifier = Modifier
                        .fillParentMaxWidth(1f)
                        .fillParentMaxHeight(0.8f),
                        painter = item.image,
                        contentDescription = item.description,
                        text = item.description
                    )
                }
            }
        }
/*
        Box(
            modifier = Modifier
            .constrainAs(spacer){
                bottom.linkTo(parent.bottom, margin = 8.dp)
               // absoluteRight.linkTo(parent.absoluteRight, margin = 24.dp)
              //  absoluteLeft.linkTo(parent.absoluteLeft , margin = 24.dp)
            }
            .padding(horizontal = 24.dp)
        ){
            MyDivider(height = 2.dp)
            //Text("I am here")
        }
*/



    }
  /*      LazyVerticalGrid(
            state = listState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
            columns = GridCells.Fixed(2)
        ){
            items(list){ item ->
                EachItem(
                    text = item,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier= Modifier.height(4.dp))
            }
        }*/

}


//@Preview
@Composable
fun PreviewEachItem() {
    SpecialSection()
}


@Composable
fun EachItem(
    modifier: Modifier = Modifier.size(128.dp),
    painter:Painter = painterResource(R.drawable.coffee_animation),
    contentDescription:String = "null",
    text:String = "Title of Item",
    screenHeight:Dp = 400.dp,
    screenWidth: Dp = 360.dp
) {
    val radius = 10.dp

    androidx.compose.material.Card(
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
                contentDescription = contentDescription)
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

@Composable
fun EachItemTwo(
    modifier: Modifier = Modifier.size(128.dp),
    painter:Painter = painterResource(R.drawable.coffee_animation),
    contentDescription:String = "null",
    text:String = "Title of Item",
    screenHeight:Dp = 400.dp,
    screenWidth: Dp = 360.dp
) {
    val radius = 10.dp

    androidx.compose.material.Card(
        modifier = modifier,
        shape = RoundedCornerShape(radius),
        elevation = 8.dp
    ) {
        Box{
            ConstraintLayout (
            ) {
                val (imageLayout, textLayout) = createRefs()

                Image(
                    modifier = Modifier
                        .padding(8.dp)
                        .constrainAs(imageLayout){
                            top.linkTo(parent.top)
                        },
                            //.fillMaxWidth()
                            //.fillMaxSize()

                    contentScale = ContentScale.FillBounds,
                    painter = painter,
                    contentDescription = contentDescription)
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                        .background(ShoppingColors.LightColors.primary)
                        .constrainAs(textLayout){
                            bottom.linkTo(parent.bottom, margin = 8.dp)
                        //https://stackoverflow.com/questions/64171607/how-to-use-bias-in-constraint-layout-compose
                        // linkTo(bias = 0f)
                        },
                )
            }
        }

    }


}

//***Footer***
@Composable
fun Footer(modifier: Modifier) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 50.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small),
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
            modifier = modifier.weight(3f),
            onClick = {},
            painter = painterResource(R.drawable.coffee_menu_btn)
        )

        fastImageButton(
            modifier = modifier.weight(3f),
            onClick = {},
            painter = painterResource(R.drawable.coffee_home_btn)
        )

        fastImageButton(
            modifier = modifier.weight(3f),
            onClick = {},
            painter = painterResource(R.drawable.coffee_cart_btn)
        )
    }
}

@Composable
fun fastImageButton(modifier:Modifier = Modifier, onClick: () -> Unit = {}, painter: Painter){
    Box(
        modifier = modifier
            .clickable { },
        contentAlignment = Alignment.Center,

        ) {
        IconButton(
            onClick = {onClick}
        ){
            androidx.compose.material3.Icon(
                painter = painter,
                contentDescription = "Coffee Menu Button"
            )
        }
    }
}

private fun onClickCard() {
    Log.d("ShoppingFragment", "onClickCard: Hello")
}


@Composable
fun CustomizedText() {

    Text(
        buildAnnotatedString {
            append("welcome to ")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.W900,
                    color = Color(0xFF4552B8)
                )
            ) {
                append("Jetpack Compose Playground")
            }
        }
    )
    Text(
        buildAnnotatedString {
            append("Now you are in the ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                append("Card")
            }
            append(" section")
        }
    )

}

@Composable
fun CardDemo(onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .defaultMinSize(minHeight = 208.dp)
            .fillMaxWidth()
            .padding(0.dp)
    ) {

        Card(
            modifier = Modifier
                //.width(IntrinsicSize.Max)
                //.size(width = 150.dp, height=200.dp)
                .defaultMinSize(minWidth = 150.dp, minHeight = 230.dp)
                .padding(1.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .clickable { onClick },
            elevation = 10.dp,
            backgroundColor = ShoppingColors.GreenWhite,
            contentColor = Color.DarkGray,


            ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .background(Color.White)
                    .border(1.dp, androidx.compose.material3.MaterialTheme.colorScheme.primary),

                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Image(
                    painter = painterResource(drawable.ic_face_48dp),
                    modifier = Modifier
                        .weight(8F)
                        .background(color = ShoppingColors.DarkGrey),
                    contentDescription = "Item Picture"
                )

                Text(
                    modifier = Modifier
                        .weight(2F),
                    text = "Hello Bro"
                )
            }
        }
    }

}



