package com.example.portfolio.feature_shopping.presentation.detail

import android.view.Window
import androidx.compose.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.presentation.cart.CartStateViewModel
import com.example.portfolio.feature_shopping.presentation.main.ShoppingItemStateViewModel


@Composable
fun ShoppingDetailScreen (
    modifier: Modifier = Modifier,
    screenHeight:Dp = 640.dp,
    screenWidth: Dp = 360.dp,
    window: Window ? = null,
    navController:NavController,
    selectedItemId:String,
    itemStateVM:ShoppingItemStateViewModel,
    cartStateViewModel: CartStateViewModel,
    isTablet:Boolean
){
    println("ShoppingItemStateVM - $itemStateVM")
    val selectedItem = itemStateVM.getSelectedItem(selectedItemId.toInt())

    Scaffold(){
        Column(
            modifier= modifier.background(color = MaterialTheme.colorScheme.background)
        ){
            //com.example.portfolio.feature_shopping.presentation.main.Header() // Skip in this section
            if (selectedItem != null) {
                BodyContent(
                    modifier = Modifier
                        .background(color = Color.Black),
                    selectedItem = selectedItem,
                    addToCart = cartStateViewModel::addItem
                )
            }
/*            Footer(
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
                navController = navController
            )*/
        }
    }
}

/*
@Composable
fun ShoppingDetailScreen_old (
    modifier: Modifier = Modifier,
    screenHeight:Dp = 640.dp,
    screenWidth: Dp = 360.dp,
    window: Window ? = null,
    navController:NavController,
    selectedItemId:String,
    itemStateVM:ShoppingItemStateViewModel,
    cartStateViewModel: CartStateViewModel,
    isTablet:Boolean
){
    println("ShoppingItemStateVM - $itemStateVM")
    val selectedItem = itemStateVM.getSelectedItem(selectedItemId.toInt())

*/
/*
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )*//*
    Column(
        modifier= modifier.background(color = MaterialTheme.colorScheme.background)
    ){
        //com.example.portfolio.feature_shopping.presentation.main.Header() // Skip in this section
        if (selectedItem != null) {
            Detail_Body(
                modifier = Modifier.weight(0.9f),
                screenHeight = screenHeight,
                screenWidth = screenWidth,
                selectedItem = selectedItem,
                itemStateVM = itemStateVM,
                cartStateVM = cartStateViewModel
            )
        }
        Footer(
            modifier = when(isTablet){
                false->{
                    Modifier.weight(0.08f)
                        .defaultMinSize(minHeight = 50.dp)
                    //.fillMaxWidth()

                }
                true ->{
                    Modifier.weight(0.10f)
                        .defaultMinSize(minHeight = 50.dp)
                }
            },
            navController = navController
        )
    }
}
*/


@Preview
@Composable
fun BodyContent(
    modifier:Modifier = Modifier,
    painter: Painter = painterResource(R.drawable.coffee_animation),
    context:Context = LocalContext.current,
    selectedItem:SellingItem = SellingItem(title = "Image title"),
    addToCart:(item:SellingItem) ->Unit = { }
) {
    //val webImage= rememberAsyncImagePainter(model = selectedItem.imageUrl)
    ConstraintLayout(modifier = modifier){

        val (image, imageTitle, keyDescription, controlCard) = createRefs()

        //image
        Card(
            modifier = Modifier
                .constrainAs(image){
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.absoluteLeft, margin = 16.dp)
                    end.linkTo(parent.absoluteRight, margin = 16.dp)
                }
                .background(color = Color.DarkGray, shape = RoundedCornerShape(16.dp))
                .border(4.dp, color = Color.DarkGray, shape = RoundedCornerShape(16.dp))
                .clip(shape = ShapeDefaults.ExtraLarge)
                .fillMaxWidth(0.7f)
        ){
            if (selectedItem.imageUrl != ""){
                SubcomposeAsyncImage(
                    model = selectedItem.imageUrl,
                    contentDescription = selectedItem.description,
                    loading = {CircularProgressIndicator()},
                )
            } else Image(
                painter = painter,
                contentDescription = null
            )

        }

        //image title
        Text(
            modifier = Modifier
                .constrainAs(imageTitle){
                    top.linkTo(image.bottom, margin = 32.dp)
                    start.linkTo(parent.absoluteLeft, margin = 16.dp)
                    end.linkTo(parent.absoluteRight, margin = 16.dp)
                },
            text = selectedItem.title,
            fontFamily = FontFamily.Cursive,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            textDecoration = null,
            softWrap = false,
            maxLines = 1,
        )

        ItemDescriptionCards(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(150.dp)
                .constrainAs(keyDescription){
                    top.linkTo(imageTitle.bottom,margin = 100.dp)
                    start.linkTo(parent.absoluteLeft,margin = 4.dp)
                    end.linkTo(parent.absoluteRight,margin = 4.dp)
                }
                .padding(16.dp)
        )
/*
        ItemPriceAndCart(
            modifier = Modifier
                .constrainAs(controlCard){
                    bottom.linkTo(parent.bottom, margin = 4.dp)
                    start.linkTo(parent.absoluteLeft,margin = 4.dp)
                    end.linkTo(parent.absoluteRight,margin = 4.dp)
                    top.linkTo(keyDescription)
                }
                .fillMaxWidth(0.95f)
                .padding(all = 12.dp)
            ,
            price = "$${selectedItem.price}",
            selectedItem = selectedItem,
            addToCart = addToCart
        )*/
    }
}

@Composable
fun ItemDescriptionCards(
    modifier:Modifier = Modifier,
    descriptionOne: String = "Dark",
    descriptionTwo: String = "Smooth",
    descriptionThree: String = "Creamy"
) {
    val spacerWeight = 0.03f
    val BoxWeight = 0.3f
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {

        BoxDescriptionItem(
            modifier = Modifier.weight(BoxWeight),
            painter = painterResource(id = R.drawable.coffee_bean),
            text = descriptionOne
        )
        Spacer(modifier = Modifier.width(8.dp))
        BoxDescriptionItem(
            modifier = Modifier.weight(BoxWeight),
            painter = painterResource(id = R.drawable.coffee_bean),
            text = descriptionTwo
        )
        Spacer(modifier = Modifier.width(8.dp))

        BoxDescriptionItem(
            modifier = Modifier
                .weight(BoxWeight)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
            painter = painterResource(id = R.drawable.coffee_bean),
            text = descriptionThree
        )

    }
}

@Composable
fun BoxDescriptionItem(
    modifier:Modifier = Modifier
        .height(80.dp)
        .width(50.dp),
    painter:Painter = painterResource(id = R.drawable.coffee_bean),
    text:String = "Dark",
) {
    Card(
        modifier = modifier,
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            //modifier = Modifier.padding(4.dp),
        ){
            Icon(
                painter = painter ,
                contentDescription = null,
                modifier = Modifier.weight(0.65f)
            )
            Text(
                text = text,
                modifier = Modifier.weight(0.15f)
            )
            Spacer(modifier = Modifier.weight(0.2f))
        }
    }
}


@Preview
@Composable
fun ItemPriceAndCart(
    modifier: Modifier = Modifier,
    price:String = "7.99",
    selectedItem: SellingItem = SellingItem(),
    addToCart:(item:SellingItem) -> Unit = {} //from cartviewmodel.add
) {
    val quantity = remember{mutableStateOf(1)}
    val selectedItem = remember{mutableStateOf(selectedItem)}

    Card(modifier = modifier){
        Column{

            Row{
                Column(){
                    Text(
                        text = "Coffee Bean By Christoph",
                        fontSize = 24.sp,
                        fontWeight= FontWeight.SemiBold
                    )
                    Text(
                        text= "Size: 16 fl oz/ 1 lb",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
            Row{
                Text(
                    text = "$$price",
                    fontSize = 32.sp,
                    fontWeight= FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(50.dp))
                QuantityControlButton(
                    quantity = quantity.value,
                    modifier = Modifier,
                    removeAction = {
                        if(selectedItem.value.quantity > 1) selectedItem.value.quantity--
                    },
                    addAction = {
                        if(selectedItem.value.quantity <99) selectedItem.value.quantity++
                    }
                )
                Spacer(modifier = Modifier.width(24.dp))
                Button(
                    onClick = {addToCart(selectedItem.value)}
                ){
                    Text(
                        text = "Cart"
                    )
                }
            }
        }


    }
}

@Composable
fun QuantityControlButton(
    quantity:Int = 0,
    modifier:Modifier = Modifier,
    removeAction:()->Unit ={},
    addAction:()->Unit ={}
) {
    val currentQuantity = remember{ mutableStateOf(quantity)}
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 4.dp, color = Color.Gray)
    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .defaultMinSize(minWidth = 100.dp)
        ){
            Button(
                onClick = {
                    removeAction()
                    if(currentQuantity.value > 1) currentQuantity.value --
                },
                modifier = Modifier
                    .defaultMinSize(minWidth = 25.dp)
                    .background(color = Color.Transparent)

            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_remove_24),
                    contentDescription = "remove",
                    modifier = Modifier
                        .background(color = Color.Transparent)
                )
            }
            Text(
                text = "${currentQuantity.value}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .border(width = 4.dp, color= Color.Gray)
                ,
            )

            Button(
                onClick = {
                    addAction()
                    if(currentQuantity.value < 99) currentQuantity.value++
                },
                modifier = Modifier
                    .defaultMinSize(minWidth = 25.dp)
                    .background(color = Color.Transparent)
                ,
                ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "remove",
                    modifier = Modifier
                        .background(color = Color.Transparent)

                )
            }
        }

    }

}


/*
@Composable
fun Detail_Body_old(
    modifier:Modifier = Modifier.padding(16.dp),
    painter: Painter = painterResource(R.drawable.coffee_animation),
    screenHeight: Dp,
    screenWidth: Dp,
    context:Context = LocalContext.current,
    selectedItem:SellingItem,
    itemStateVM:ShoppingItemStateViewModel, //= hiltViewModel<ShoppingItemStateViewModel>()
    cartStateVM:CartStateViewModel
) {
        ImageFrame(modifier
            .fillMaxSize()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(0.dp),
            bodyContent = {
                Column(modifier.fillMaxSize()){
                    selectedItem.let{
                        Detail_ImageAndTitle(
                            modifier.weight(5f).padding(top = 0.dp, bottom= 0.dp),
                            modelUrl = it.imageUrl,
                            description = it.description,
                            itemTitle = it.title,
                        )
                    } ?: Detail_ImageAndTitle(
                        modifier.weight(5f).padding(top = 0.dp, bottom= 0.dp),
                    )
                    Detail_ProductInfo(
                        modifier
                            .weight(5f)
                            .padding(start = 18.dp, end = 18.dp,top = 16.dp, bottom= 8.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            },
            bodyBotContent = {
                Detail_AddItemFloatBtn(
                    modifier = Modifier,
                    selectedItem = selectedItem,
                    cartStateVM = cartStateVM
                )
            }
        )
}*/

/**
 * Simply
 */

//@Preview(widthDp = 360, heightDp = 640)
@Composable
fun ImageFrame(
    modifier:Modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
    painter:Painter = painterResource(R.drawable.coffee_mug_cup_frame),
    bodyContent: @Composable () -> Unit = {Text("TestLazyColumn Section")},
    bodyBotContent: @Composable () -> Unit = {Text("Bottom Calculation Section")},
){
    Surface(
        modifier = modifier,
        //modifier.background(color = MaterialTheme.colorScheme.background),
        //color= MaterialTheme.colorScheme.background
    ){
        Card (
            //modifier = Modifier.fillMaxSize(),//.background(color = MaterialTheme.colorScheme.background),
            elevation = 8.dp,
        ){
            Image(
                modifier= Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                painter = painter,
                contentDescription = "Mug Cup ImageFrame",

            )
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth(1.0f)
                    .fillMaxHeight(0.70f)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.90f)
                        .fillMaxHeight(0.85f)
                        .clip(shape = RoundedCornerShape(8.dp))
                    //.padding(top = 2.dp, bottom= 8.dp, start = 8.dp, end = 18.dp),
                ){
                    Box(
                        modifier = Modifier
                            .weight(9.3f)
                            .fillMaxHeight()
                            .fillMaxWidth()

                    ){
                        bodyContent()
                    }

                    //com.example.portfolio.feature_shopping.presentation.main.MyDivider(height = 0.dp)

                    Row(modifier = Modifier
                        .weight(0.70f)
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp)
                    ){
                        bodyBotContent()
                    }/*
                    Box(modifier = Modifier.weight(0.85f).padding(top = 18.dp, bottom = 0.dp)){
                        bodyBotContent()
                    }
                    */
                }
            }
        }
    }


}

/*
@Preview(widthDp = 360, heightDp = 640)
@Composable
fun ImageFrame_old(
    modifier:Modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
    painter:Painter = painterResource(R.drawable.coffee_mug_cup_frame),
    bodyContent: @Composable () -> Unit = {Text("TestLazyColumn Section")},
    bodyBotContent: @Composable () -> Unit = {Text("Bottom Calculation Section")},
){
    Surface(
        modifier = modifier,
        //modifier.background(color = MaterialTheme.colorScheme.background),
        //color= MaterialTheme.colorScheme.background
    ){
        Card (
            //modifier = Modifier.fillMaxSize(),//.background(color = MaterialTheme.colorScheme.background),
            elevation = 8.dp,
        ){
            Image(
                modifier= Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                painter = painter,
                contentDescription = "Mug Cup ImageFrame",

            )
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth(1.0f)
                    .fillMaxHeight(0.70f)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.90f)
                        .fillMaxHeight(0.85f)
                        .clip(shape = RoundedCornerShape(8.dp))
                    //.padding(top = 2.dp, bottom= 8.dp, start = 8.dp, end = 18.dp),
                ){
                    Box(
                        modifier = Modifier
                            .weight(9.3f)
                            .fillMaxHeight()
                            .fillMaxWidth()

                    ){
                        bodyContent()
                    }

                    //com.example.portfolio.feature_shopping.presentation.main.MyDivider(height = 0.dp)

                    Row(modifier = Modifier
                        .weight(0.70f)
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp)
                    ){
                        bodyBotContent()
                    }*/
/*
                    Box(modifier = Modifier.weight(0.85f).padding(top = 18.dp, bottom = 0.dp)){
                        bodyBotContent()
                    }
                    *//*

                }
            }
        }
    }


}
*/

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Detail_ImageAndTitle(
    modifier:Modifier = Modifier,
    painter:Painter = painterResource(R.drawable.coffee_animation),
    modelUrl:String = "",
    description:String = "Coffee Image",
    itemTitle:String = "American Coffee"
){
    Box(modifier = modifier){
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ){
                Card(
                    elevation = 8.dp,
                    modifier = Modifier
                        .weight(7f)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onTertiary,
                            shape = RoundedCornerShape(6.dp)
                        )
                ){
                    println("selected Image Url is $modelUrl")
                    if(modelUrl != ""){
                        //val onlinePainter = rememberAsyncImagePainter(model = modelUrl)
                        AsyncImage(
                            model = modelUrl,
                            contentDescription = description,
                            modifier = Modifier,
                            placeholder = painter,
                        )
                        /*Canvas(
                            modifier = Modifier,
                            contentDescription = description
                        ){
                            with(onlinePainter){
                                draw()
                            }
                        }*/
                    }else{
                        Image(
                            painter =painter,
                            contentDescription = description,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = itemTitle,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

    }
}

/*

@Composable
fun Detail_ImageAndTitle(
    modifier:Modifier = Modifier,
    painter:Painter = painterResource(R.drawable.coffee_animation),
    description:String = "Coffee Image",
    itemTitle:String = "American Coffee"
){
    Box(modifier = modifier){
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ){
                Card(
                    elevation = 8.dp,
                    modifier = Modifier
                        .weight(7f)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onTertiary,
                            shape = RoundedCornerShape(6.dp)
                        )
                ){
                    Image(
                        painter =painter,
                        contentDescription = description,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = itemTitle,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

    }
}
*/

@Composable
fun Detail_ProductInfo(modifier:Modifier = Modifier.verticalScroll(rememberScrollState()), data: SelectedData = SelectedData(1)){

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
    ){
        Text("Country:  ${data.country}")
        Spacer(modifier.height(4.dp))
        Text("Bean Type:  ${
            when (data.type) {
                BeanType.Espresso -> {"Espresso Roast"}
                BeanType.Dark -> {"Dark Roast"}
                BeanType.Medium -> {"Medium Roast"}
                BeanType.Light -> {"Light Roast"}
                //else -> {"Dark Roast"}
            }
        }")
        Spacer(modifier.height(4.dp))
        Text("Information: \n   ${data.information}")
        Spacer(modifier.height(8.dp))
    }
}

@Composable
fun Detail_AddItemFloatBtn(
    modifier:Modifier = Modifier,
    selectedItem: SellingItem,
    cartStateVM: CartStateViewModel //= hiltViewModel<CartStateViewModel>()

){
     Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(modifier = Modifier
            .weight(8f)
            .padding(start = 16.dp)){
            Text(text="$${selectedItem.price?:0.99}", fontSize = 30.sp)
        }
        Box(modifier = Modifier.weight(2f)
        ){
            Shopping_FloatBtn(
                onClick = {
                    cartStateVM.addItem(selectedItem)
                }
            )
        }
    }
}

@Composable
fun Shopping_FloatBtn(modifier:Modifier = Modifier, onClick:() -> Unit = {}){
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        contentColor = MaterialTheme.colorScheme.tertiary,
        backgroundColor = MaterialTheme.colorScheme.onTertiary
    ){
        Icon(Icons.Filled.Add, "Adding Button with Plus Icon")
    }
}

data class SelectedData(
    val id:Int = 0,
    val country:String = "North America",
    val type: BeanType = BeanType.Dark,
    val information:String =
            "Once upon a time, there was a coffee bean grow from Northern America." +
            "It was so flavorful and gave so much energy to people whoever had a chance to sip of it" +
            "This was one of many reasons how American worked harder than other countries."


)
enum class BeanType{
    Espresso, Dark, Medium, Light
}
/*
@Composable
fun BorderImage(modifier:Modifier = Modifier, painter:Painter = painterResource(R.drawable.coffee_animation)){
    Card(
        modifier = modifier,
        border = BorderStroke(width= 2.dp, color = MaterialTheme.colorScheme.onTertiary)
    ){
        Image(
            painter = painter,
            contentDescription = "Image with Border",
            contentScale = ContentScale.Fit
        )
    }
}*/
