package com.example.portfolio.feature_shopping.presentation.cart

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.CartItem
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.presentation.payment.PaymentDialogScreen
import com.example.portfolio.feature_shopping.presentation.ui.theme.Brown_300
import com.example.portfolio.feature_shopping.presentation.utils.CartUIEvent
import com.example.portfolio.feature_shopping.presentation.utils.Helper
import com.example.portfolio.feature_shopping.presentation.utils.Helper.formatDoubleToString
import com.example.portfolio.feature_shopping.presentation.utils.Helper.nonScaledSp
import com.example.portfolio.feature_shopping.presentation.utils.MyDivider
import com.example.portfolio.feature_shopping.presentation.utils.Screens

@Composable
fun ShoppingCartScreen(
    modifier:Modifier = Modifier,
    navController:NavController,
    cartVM: CartStateViewModel,
    cartUIState: Cart = Cart(),
    onEventChange:(CartUIEvent)->Unit,
    isTablet:Boolean,
){
    println("ShoppingCartScreen")
    Scaffold(
    ){
        topBarContent(navController = navController)
        ShoppingCartBody(
            modifier = Modifier
                .fillMaxSize(1.0f)
                .padding(vertical = 25.dp, horizontal = 16.dp),
            cartUIState = cartVM.cartUIState,
            onEventChange = cartVM::setCartUIEvent,
            isTablet = isTablet,
        )
    }

    BackHandler() {
        navController.navigate(Screens.Main.rout)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarContent(modifier:Modifier = Modifier.fillMaxWidth(), navController: NavController){

    ConstraintLayout(modifier = modifier) {

        val (backBtn, title) = createRefs()

        TopAppBar(
            modifier = Modifier.constrainAs(backBtn){
                top.linkTo(parent.top,margin = 8.dp)
                start.linkTo(parent.start,margin = 8.dp)
                bottom.linkTo(parent.bottom,margin = 8.dp)
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            title = {stringResource(R.string.cart_title)},
            navigationIcon = {
                if(navController.previousBackStackEntry != null){
                    IconButton(onClick = {navController.navigateUp()}){
                        androidx.compose.material.Icon(
                            imageVector =  Icons.Filled.ArrowBack,
                            contentDescription = "Back to previous page"
                        )
                    }
                }else{
                    println("back button got some issue")
                }
            }
        )

        Text(
            modifier = Modifier.constrainAs(title){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            text= stringResource(R.string.cart_title)
        )
    }

}

@Composable
fun ShoppingCartBody(
    modifier:Modifier = Modifier.fillMaxSize(),
    //cartStateVM: CartStateViewModel,
    cartUIState: Cart = Cart(subTotal = "$6.99"),
    onEventChange:(CartUIEvent)-> Unit = {},
    subTotal: String = Helper.formatDoubleToString(cartUIState.subTotal.toDouble()),
    isTablet: Boolean = false,
) {
    //val isTablet =  if(Helper.isRoated()) true else booleanResource(id = R.bool.is_tablet)

    Box(modifier = modifier){
        ConstraintLayout(modifier=modifier){
            val (ListOrder, SubTotal, OrderBtn) = createRefs()

            CartListAndSubTotal(
                modifier= Modifier
                    .fillMaxHeight(0.55f)
                    .fillMaxWidth()
                    .constrainAs(ListOrder) {
                        absoluteLeft.linkTo(parent.absoluteLeft, margin = 4.dp)
                        absoluteRight.linkTo(parent.absoluteRight, margin = 4.dp)
                        top.linkTo(parent.top, margin = 16.dp)
                        //top.linkTo(OrderBtn.top)
                        //bottom.linkTo(parent.bottom, margin = 8.dp)
                    },
                cartState = cartUIState,
                onEventChange= onEventChange,
            )


            //This is place for subtotal
            SubTotal(
                modifier = Modifier
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth(0.75f)
                    .constrainAs(SubTotal) {
                        absoluteLeft.linkTo(parent.absoluteLeft, margin = 8.dp)
                        absoluteRight.linkTo(parent.absoluteRight, margin = 8.dp)
                        top.linkTo(ListOrder.bottom, margin = 8.dp)
                    },
                subTotal = subTotal
            )

            //In order to stay top of ImageFrame, it need to be called after ImageFrame

            OrderButton(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(IntrinsicSize.Min)
                    .constrainAs(OrderBtn) {
                        absoluteLeft.linkTo(parent.absoluteLeft, margin = 8.dp)
                        absoluteRight.linkTo(parent.absoluteRight, margin = 8.dp)
                        top.linkTo(SubTotal.bottom, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        //inkTo(parent.top, ListOrder.top, bias = 0.01f)
                    },
                cartUIState = cartUIState,
                onClick = onEventChange,
                isTablet = isTablet
            )
        }
    }
}

@Composable
fun SubTotal(
    modifier:Modifier = Modifier.fillMaxWidth(0.7f),
    subTotal:String = "0.00",
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 16.dp)
        ){
            Text(
                //textAlign = TextAlign.Start,
                text = "SubTotal:",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier= Modifier.weight(1f)
            )
            Text(
                text = "$${subTotal}"
            )
        }
        MyDivider(
            Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterHorizontally))
    }
}

@Composable
fun OrderButton(
    modifier:Modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp),
    text:String = "Place Order",
    cartUIState: Cart,
    onClick: (CartUIEvent)->Unit = {},
    isTablet:Boolean
){
    var openDialog by remember{ mutableStateOf(false) }
    FloatingActionButton(
        onClick = {
            //println("openDialog from parent UI - ${openDialog}")
            openDialog = true
        },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Brown_300,
        modifier = modifier,
    ){
        Text(
            text = text,
            textAlign = TextAlign.Justify,
            color = Color.White,
            fontSize = 18.nonScaledSp,//18.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        if(openDialog){

            println("OrderButton is clicked")
            PaymentDialogScreen(
                shouldOpenDialog = openDialog,
                onDismissedCalled = {
                    openDialog = !openDialog

                },
                //cartStateViewModel = cartVM,
                cartState = cartUIState, //cartData  = cartVM.cartUIState,
                removeItems = {
                    onClick(CartUIEvent.RemoveAllFromCart)
                },// {cartVM.removeAllItem()},
                onComplete = {openDialog = !openDialog},
                isTablet = isTablet
            )
        }
    }
}

@Preview
@Composable
fun CartListAndSubTotal(
    modifier:Modifier = Modifier,
    cartState: Cart = Cart(),
    items: MutableMap<SellingItem, Int> = cartState.items,
    totalQuantity:Int = cartState.totalQuantity,
    subTotal:String = Helper.formatDoubleToString(cartState.subTotal.toDouble()),
    onEventChange: (CartUIEvent) -> Unit = {},
    list:List<Pair<SellingItem, Int>> = cartState.items.toList(),
    //For test use
    listCartItems:List<CartItem> = listOf(
        CartItem(id = 0, count = 1, price = 1.99, itemTitle = "Korea Coffee", image = painterResource(R.drawable.coffee_animation),),
        CartItem(id = 1, count = 1, price = 2.99, itemTitle = "America Coffee", image = painterResource(R.drawable.coffee_animation),),
        CartItem(id = 2, count = 1, price = 3.99, itemTitle = "Brazil Coffee", image = painterResource(R.drawable.coffee_animation),),
        CartItem(id = 3, count = 1, price = 4.99, itemTitle = "Mexico Coffee", image = painterResource(R.drawable.coffee_animation),),
        CartItem(id = 4, count = 1, price = 5.99, itemTitle = "England Coffee", image = painterResource(R.drawable.coffee_animation),),
        CartItem(id = 5, count = 1, price = 6.99, itemTitle = "Italia Coffee", image = painterResource(R.drawable.coffee_animation),),
    )
){
/*    var SellingItem = cart.SellingItem
    var totalSize = SellingItem.size
    var list = cart.SellingItem.toList()*/
    println("CartListAndSubTotal")

    Box(
        modifier = modifier,
    ){
        LazyColumn(
            modifier = Modifier.background(color = Color.Transparent)
        ){
            items(items.toList() /*cartState.SellingItem.toList()*/){(SellingItem, Quantity)->
                CartEachItem(
                    modifier = Modifier.padding(bottom = 16.dp),
                    curQuantity = Quantity,//SellingItem.quantity,
                    //cartVM = cartVM,
                    cartState = cartState,
                    selectedItem = SellingItem,
                    productTitle = SellingItem.title,
                    removeListener = {onEventChange(CartUIEvent.RemoveFromCart(SellingItem))},
                    addListener = {onEventChange(CartUIEvent.AddToCart(SellingItem))},
                    reduceListener = {onEventChange(CartUIEvent.ReduceFromCart(SellingItem))},
                )
            }
        }
    }
}

@Composable
fun CartEachItem(
    modifier:Modifier = Modifier,
    painter: Painter = painterResource(R.drawable.coffee_animation),
    cartState: Cart = Cart(),
    selectedItem: SellingItem  = SellingItem(),//? = null,
    curQuantity:Int = selectedItem.quantityInCart,//= 1,
    itemTotal:String = formatDoubleToString(selectedItem.itemTotal),//6.99,
    itemTotalTextSize: TextUnit = 24.sp,
    productTitle:String = "I am the title",
    removeListener: () -> Unit = {},
    addListener:() -> Unit ={},
    reduceListener:()->Unit={}
){
    println("CartEachItem")
    val isTablet=
        if(Helper.isRoated()) true
        else booleanResource(id = R.bool.is_tablet)
    when(isTablet){
        true -> {
            Surface(color = Color.Transparent){
                Row(
                    modifier = modifier
                        .background(color = Color.Transparent)
                        .defaultMinSize(minHeight = 100.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    //Selected Item Image Section
                    Box(
                        modifier = Modifier
                            .weight(0.35f)
                            .padding(start = 8.dp)
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Surface(
                                // color = Color.Black.copy(alpha=0.0f)
                            ){
                                selectedItem?.let{
                                    AsyncImage(
                                        modifier = Modifier
                                            .border(
                                                width = 2.dp,
                                                shape = RoundedCornerShape(8.dp),
                                                color = MaterialTheme.colorScheme.onTertiary
                                            )
                                            .clip(shape = RoundedCornerShape(8.dp)),
                                        model = selectedItem.imageUrl,
                                        contentDescription = selectedItem.description?: "Cart Item Image",
                                        placeholder = painterResource(R.drawable.coffee_animation),
                                    )
                                } ?:
                                Image(
                                    modifier = Modifier
                                        .border(
                                            width = 2.dp,
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.onTertiary
                                        )
                                        .clip(shape = RoundedCornerShape(8.dp)),
                                    painter = painter,
                                    contentDescription = "Cart Item Image",
                                    //contentScale = ContentScale.Fit

                                )
                            }
                            Text(
                                textAlign = TextAlign.Center,
                                text = productTitle,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }

                    //Counting Section
                    Box(
                        modifier = Modifier.weight(0.20f),
                        contentAlignment = Alignment.BottomCenter
                    ){
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Row(
                                //modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                //reduce
                                IconButton(
                                    onClick = reduceListener,
                                    enabled = true,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = contentColorFor(MaterialTheme.colorScheme.onTertiary)
                                    ),

                                    ){
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Remove Icon",
                                        tint = MaterialTheme.colorScheme.onTertiary
                                    )
                                }

                                Text(
                                    text = curQuantity.toString(),
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 18.nonScaledSp
                                )
                                IconButton(
                                    onClick = addListener,
                                    enabled = true,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = contentColorFor(MaterialTheme.colorScheme.onTertiary)
                                    )
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Remove Icon",
                                        tint = MaterialTheme.colorScheme.onTertiary
                                    )
                                }
                            }

                            TextButton(
                                onClick = removeListener,
                            ){
                                Text(
                                    text ="remove",
                                    fontSize = 12.nonScaledSp,
                                    color = Color.Blue
                                )
                            }
                        }
                    }

                    //Calculation Section/Price Section
                    Box(
                        modifier = Modifier.weight(0.3f),
                        contentAlignment = Alignment.Center
                    ){
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$${selectedItem?.let {
                                    cartState.items[it]?.times(it.price) ?: it.itemTotal              //!!.times(it.price)
                                }}", //${selectedItem?.let { cartVM.getCurItemPrice(it) }}",
                                fontSize = itemTotalTextSize
                            )
                        }
                    }
                }
            }
        }
        false -> {
            Surface(color = Color.Transparent){
                Row(
                    modifier = modifier
                        .background(color = Color.Transparent)
                        .defaultMinSize(minHeight = 100.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    //Selected Item Image Section
                    Box(
                        modifier = Modifier
                            .weight(0.40f)
                            .padding(start = 8.dp)
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Surface(
                                // color = Color.Black.copy(alpha=0.0f)
                            ){
                                selectedItem?.let{
                                    AsyncImage(
                                        modifier = Modifier
                                            .border(
                                                width = 2.dp,
                                                shape = RoundedCornerShape(8.dp),
                                                color = MaterialTheme.colorScheme.onTertiary
                                            )
                                            .clip(shape = RoundedCornerShape(8.dp)),
                                        model = selectedItem.imageUrl,
                                        contentDescription = selectedItem.description?: "Cart Item Image",
                                        placeholder = painterResource(R.drawable.coffee_animation),
                                    )
                                }

                            }
                            Text(
                                textAlign = TextAlign.Center,
                                text = productTitle
                            )
                        }
                    }

                    //Counting Section
                    Box(
                        modifier = Modifier.weight(0.25f),
                        contentAlignment = Alignment.BottomCenter
                    ){
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Row(
                                //modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                //reduce
                                IconButton(
                                    onClick = reduceListener,
                                    enabled = true,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = contentColorFor(MaterialTheme.colorScheme.onTertiary)
                                    ),

                                    ){
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Remove Icon",
                                        tint = MaterialTheme.colorScheme.onTertiary
                                    )
                                }

                                Text(
                                    text = curQuantity.toString(),
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 18.nonScaledSp
                                )
                                IconButton(
                                    onClick = addListener,
                                    enabled = true,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = contentColorFor(MaterialTheme.colorScheme.onTertiary)
                                    )
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Remove Icon",
                                        tint = MaterialTheme.colorScheme.onTertiary
                                    )
                                }
                            }

                            TextButton(
                                onClick = removeListener,
                            ){
                                Text(
                                    text ="remove",
                                    fontSize = 12.nonScaledSp,
                                    color = Color.Blue
                                )
                            }
                        }
                    }

                    //Calculation Section/Price Section
                    Box(
                        modifier = Modifier.weight(0.25f),
                        contentAlignment = Alignment.BottomCenter
                    ){
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$${selectedItem?.let {
                                    cartState.items[it]?.times(it.price)
                                        ?.let { it1 -> Helper.formatDoubleToString(it1) }
                                }}",
                                fontSize = itemTotalTextSize
                            )
                        }
                    }
                }
            }
        }
    }


}
