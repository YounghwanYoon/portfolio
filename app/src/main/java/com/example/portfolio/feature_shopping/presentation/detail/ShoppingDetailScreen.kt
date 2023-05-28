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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
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
import com.example.portfolio.feature_shopping.presentation.utils.CartUIEvent
import com.example.portfolio.feature_shopping.presentation.utils.ShoppingColors


@OptIn(ExperimentalMaterial3Api::class)
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
    cartUIClicked: (CartUIEvent) -> Unit = {},
    isTablet:Boolean
){
    println("ShoppingItemStateVM - $itemStateVM")
    val selectedItem = itemStateVM.getSelectedItem(selectedItemId.toInt())

    val size = remember{mutableStateOf(IntSize.Zero)}
    println("height is ${size.value.height}")
    println("width is ${size.value.width}")

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = {/*Text(text= "app bar title")*/},
                navigationIcon = {
                    if(navController.previousBackStackEntry != null){
                        IconButton(onClick = {navController.navigateUp()}){
                            Icon(
                                imageVector =  Icons.Filled.ArrowBack,
                                contentDescription = "Back to previous page"
                            )
                        }
                    }else{
                        println("back button got some issue")
                    }
                }
            )
        }
    ){
        Column(
            modifier= modifier.onSizeChanged {
                println("current size is ${size.value.height}")
                println("current size is ${size.value.width}")
            }
        ){
            //com.example.portfolio.feature_shopping.presentation.main.Header() // Skip in this section
            selectedItem?.let{
                BodyContent(
                    modifier = Modifier
                        .fillMaxSize(),
                    selectedItem = selectedItem,
                    onAddClicked = cartUIClicked,//cartStateViewModel::addItem,
                    onCompletion = {
                        navController.navigateUp()
                    },
                    isTablet = isTablet
                )
            }
        }

    }
}


@Composable
fun BodyContent(
    modifier:Modifier = Modifier,
    painter: Painter = painterResource(R.drawable.coffee_animation),
    context:Context = LocalContext.current,
    selectedItem:SellingItem = SellingItem(title = "Image title"),
    onAddClicked:(CartUIEvent)->Unit = {},//(item: CartUIEvent, quantity:Int) -> Unit,
    onCompletion: () -> Unit = {},
    isTablet:Boolean = false
) {

    //val webImage= rememberAsyncImagePainter(model = selectedItem.imageUrl)
    var quanity: MutableState<Int> = remember{ mutableStateOf(1) }
    val isTabletMode = remember{mutableStateOf(isTablet)}

    if(!isTabletMode.value){
        ConstraintLayout(modifier = modifier){
            val (image, imageTitle, keyDescription, controlCard) = createRefs()

            //image
            Card(
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top, margin = 32.dp)
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
            Text(modifier = Modifier
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
                maxLines = 2,
            )

            ItemDescriptionCards(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(150.dp)
                    .constrainAs(keyDescription) {
                        bottom.linkTo(controlCard.top, margin = 8.dp)
                        start.linkTo(parent.absoluteLeft, margin = 4.dp)
                        end.linkTo(parent.absoluteRight, margin = 4.dp)
                    }
                    .padding(8.dp)
            )

            ItemPriceAndCart(
                modifier = Modifier
                    .constrainAs(controlCard) {
                        bottom.linkTo(parent.bottom, margin = 4.dp)
                        start.linkTo(parent.absoluteLeft, margin = 4.dp)
                        end.linkTo(parent.absoluteRight, margin = 4.dp)
                    }
                    .fillMaxWidth(0.95f)
                    .padding(all = 12.dp),
                price = "$${selectedItem.price}",
                selectedItem = selectedItem,
                eventListener = onAddClicked,
                onCompletion = onCompletion
            )
        }
    }else{
        ConstraintLayout(modifier = modifier){
            val (image, imageTitle, keyDescription, controlCard) = createRefs()

            //image
            Card(
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top, margin = 50.dp)
                        start.linkTo(parent.absoluteLeft, margin = 4.dp)
                    }
                    .background(color = Color.DarkGray, shape = RoundedCornerShape(16.dp))
                    .border(4.dp, color = Color.DarkGray, shape = RoundedCornerShape(16.dp))
                    .clip(shape = ShapeDefaults.ExtraLarge)
                    .padding(0.dp)
                    .fillMaxHeight(0.65f)
                    .fillMaxWidth(0.5f)
            ){
                if (selectedItem.imageUrl != ""){
                    SubcomposeAsyncImage(
                        contentScale = ContentScale.FillBounds,
                        model = selectedItem.imageUrl,
                        contentDescription = selectedItem.description,
                        loading = {CircularProgressIndicator()},
                    )
                } else Image(
                    contentScale = ContentScale.FillBounds,
                    painter = painter,
                    contentDescription = null
                )

            }

            //image title
            Text(modifier = Modifier
                .constrainAs(imageTitle){
                    top.linkTo(image.bottom, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                    start.linkTo(parent.absoluteLeft, margin =8.dp)
                    end.linkTo(image.absoluteRight, margin = 8.dp)
                },
                text = selectedItem.title,
                fontFamily = FontFamily.Cursive,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textDecoration = null,
                softWrap = false,
                maxLines = 3,
            )

            ItemDescriptionCards(
                modifier = Modifier
                    .fillMaxWidth(0.50f)
                    .fillMaxHeight(0.50f)
                    .height(150.dp)
                    .constrainAs(keyDescription) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(image.absoluteRight, margin = 4.dp)
                        end.linkTo(parent.absoluteRight, margin = 4.dp)
                    }
                    .padding(8.dp)
            )

            ItemPriceAndCart(
                modifier = Modifier
                    .constrainAs(controlCard) {
                        top.linkTo(keyDescription.bottom, margin = 4.dp)
                        bottom.linkTo(parent.bottom, margin = 4.dp)
                        start.linkTo(keyDescription.start, margin = 4.dp)
                        end.linkTo(parent.absoluteRight, margin = 4.dp)
                    }
                    .fillMaxWidth(0.50f)
                    .padding(all = 16.dp),
                price = "$${selectedItem.price}",
                selectedItem = selectedItem,
                eventListener = onAddClicked,
                onCompletion = onCompletion
            )
        }

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


@Composable
fun ItemPriceAndCart(
    modifier: Modifier = Modifier,
    price:String = "7.99",
    selectedItem: SellingItem = SellingItem(),
    eventListener:(CartUIEvent) -> Unit,//(item:SellingItem,quantity:Int) -> Unit,
    onCompletion: () -> Unit
) {
    val _quantity = remember{mutableStateOf(1)}
    val _selectedItem = remember{mutableStateOf(selectedItem)}

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ){
        Column(modifier= Modifier.padding(8.dp)){
            Row{
                Column(){
                    Text(
                        text = _selectedItem.value.title,
                        fontSize = 18.sp,
                        fontWeight= FontWeight.SemiBold,
                        maxLines = 3
                    )
                    Text(
                        text= "Size: 16 fl oz/ 1 lb",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraLight
                    )
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ){
                Text(
                    text = price,
                    fontSize = 32.sp,
                    fontWeight= FontWeight.Bold,
                    modifier= Modifier.weight(0.30f)
                )
                QuantityControlButton(
                    quantity = _quantity.value,
                    modifier = Modifier
                        .weight(0.30f)
                        .height(25.dp),
                    reduceQuantity = {
                         eventListener(CartUIEvent.ReduceFromCart(selectedItem))
                        //TO DO:: Need to update with VM

                        //if(_quantity.value > 1) _quantity.value--
                    },
                    addQuantity = {
                        if(_quantity.value < 99) _quantity.value++
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                //add to cart button
                Button(
                    shape = CircleShape,
                    modifier = Modifier.weight(0.20f),
                    onClick = {
                        eventListener(CartUIEvent.AddToCart(_selectedItem.value, _quantity.value))//onAddClicked(selectedItem, _quantity.value)
                        onCompletion()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ShoppingColors.Brown_700)
                ){
                    Text(
                        text = "Cart",
                        color = Color.White,
                        fontSize = 18.sp
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
    reduceQuantity:()->Unit ={},
    addQuantity:()->Unit ={}
) {
    val currentQuantity = remember{ mutableStateOf(quantity)}
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 4.dp, color = Color.Gray),
    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .defaultMinSize(minWidth = 75.dp)
        ){
            IconButton(
                //shape = RectangleShape,
                onClick = {
                    reduceQuantity()
                    if(currentQuantity.value > 1) currentQuantity.value --
                },
                //colors = ButtonDefaults.buttonColors(Color.Transparent),
                modifier = Modifier
                    .weight(0.30f)
                    .border(width = 4.dp, color = Color.Gray)
                    .padding(4.dp)

            ){
                Icon(
                    imageVector = Icons.Filled.Remove,
                    //painter = painterResource(id = R.drawable.baseline_remove_24),
                    contentDescription = "remove",
                    modifier = Modifier
                        .defaultMinSize(minWidth = 25.dp, minHeight = 25.dp)
                        //.background(color = Color.Transparent)
                )
            }
            Text(
                text = "${currentQuantity.value}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.50f)
                ,
            )

            IconButton(
                //colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                //shape = RectangleShape,
                onClick = {
                    addQuantity()
                    if (currentQuantity.value < 99) currentQuantity.value++
                },
                modifier = Modifier
                    .weight(0.30f)
                    .border(width = 4.dp, color = Color.Gray, shape = RectangleShape)
                    .padding(4.dp)

            ){
                Icon(
                    imageVector = Icons.Filled.Add,
                    //painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "remove",
                    modifier = Modifier
                        .defaultMinSize(minWidth = 25.dp, minHeight = 25.dp)
                        .background(color = Color.Transparent),
                )
            }
        }
    }
}


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

@Composable
fun Detail_ProductInfo(
    modifier:Modifier = Modifier.verticalScroll(rememberScrollState()),
    data: SelectedData = SelectedData(1)
){

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
    onClick: (CartUIEvent) -> Unit,
    //cartStateVM: CartStateViewModel //= hiltViewModel<CartStateViewModel>()

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
                    //cartStateVM.addItem(selectedItem)
                    onClick(CartUIEvent.AddToCart(selectedItem))
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