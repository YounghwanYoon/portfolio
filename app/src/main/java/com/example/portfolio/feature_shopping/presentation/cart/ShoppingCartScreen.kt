package com.example.portfolio.feature_shopping.presentation.cart

import com.example.portfolio.feature_shopping.presentation.main.Footer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.CartItem
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.presentation.detail.ImageFrame
import com.example.portfolio.feature_shopping.presentation.ui.theme.Brown_300
import com.example.portfolio.feature_shopping.presentation.ui.theme.ShoppingTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ShoppingCartScreen(
    modifier:Modifier = Modifier,
    navController:NavController,
    cartStateViewModel: CartStateViewModel
){
    ShoppingTheme{
        Column(){
            ShoppingCartBody(Modifier.weight(9f), cartStateVM = cartStateViewModel)

            Footer(Modifier.weight(1f), navController = navController, cartStateVM = cartStateViewModel)
        }
    }

}

@Composable
fun ShoppingCartHeader(modifier:Modifier = Modifier){
}

//@Preview(widthDp = 360, heightDp = 640)
@Composable
fun ShoppingCartBody(
    modifier:Modifier = Modifier,
    cartStateVM: CartStateViewModel// = hiltViewModel()
){
    println("viewmodel from cart screen- $cartStateVM")

    Surface(
        modifier = modifier,
        shadowElevation = 8.dp
    ){

        //In order to stay top of ImageFrame, it need to be called after ImageFrame
        ConstraintLayout() {
            val (OrderBtn, ListOrder) = createRefs()

            ImageFrame(
                modifier = Modifier.fillMaxHeight(),
                bodyContent = {
                    //List Order Section
                    Box(
                        modifier = Modifier.padding(top = 16.dp)
                            .constrainAs(ListOrder){
                                absoluteLeft.linkTo(parent.absoluteLeft)
                                absoluteRight.linkTo(parent.absoluteRight)
                                top.linkTo(parent.top, margin = 8.dp)
                                //top.linkTo(OrderBtn.top)
                                bottom.linkTo(parent.bottom, margin= 8.dp)
                            }
                    ){
                        CartListAndSubTotal(
                            cartVM = cartStateVM,
                        )
                    }

                }
            ){
                //This is place for subtotal
                Text("SubTotal:")
            }
            //In order to stay top of ImageFrame, it need to be called after ImageFrame
            Surface(
                modifier = Modifier
                    .constrainAs(OrderBtn) {
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                        top.linkTo(parent.top, margin = 16.dp)
                        linkTo(parent.top, ListOrder.top, bias = 0.01f)
                    },
                color = Color.Black.copy(0.1f),
                shadowElevation = 8.dp
            ) {
                Box() {
                    CoffeeButton(
                        text = "Order"
                    )
                }
            }
        }
    }

}


/*@Preview(widthDp = 360, heightDp = 640)
@Composable
fun PreviewCoffeeButton(modifier:Modifier = Modifier, text:String ="Order"){
    Box(
        modifier=modifier,
        contentAlignment = Alignment.Center
    ){
        FloatingActionButton(
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Brown_300,
            modifier =  Modifier.defaultMinSize(minWidth = 75.dp, minHeight = 50.dp)
        ){
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }

}*/

@Composable
fun CoffeeButton(modifier:Modifier = Modifier, text:String = "Undefined"){
    Surface(
        color=Color.White.copy(alpha=0.1f),
    ){
        Box(
            modifier=modifier,
            contentAlignment = Alignment.Center
        ){
            FloatingActionButton(
                onClick = {},
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Brown_300,
                modifier =  Modifier.defaultMinSize(minWidth = 75.dp, minHeight = 50.dp),
            ){
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }
    }

}




//@Preview(widthDp = 360, heightDp = 640)
@Composable
fun CartListAndSubTotal(
    modifier:Modifier = Modifier,
    cartVM:CartStateViewModel,
    cart: Cart = cartVM
        .cart
        .collectAsStateWithLifecycle(
        lifecycle = LocalLifecycleOwner.current.lifecycle,
        minActiveState = Lifecycle.State.STARTED
    ).value,
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
    var items = cart.items
    var totalSize = items.size
    var list = cart.items.toList()

    Box(
        modifier = modifier ,
    ){
        LazyColumn{
/*            items(listCartItems){ item ->
                CartEachItem(
                    modifier = Modifier.padding(bottom = 16.dp),
                    counter = item.count,
                    id = item.id,
                    painter = item.image,
                    itemTotal = (item.count.times(item.price)),
                    productTitle = item.itemTitle,
                    removeListener = {}
                )
            }*/
            items(cart.items.toList()){(SellingItem, Quantity)->
                CartEachItem(
                    modifier = Modifier.padding(bottom = 16.dp),
                    curQuantity = Quantity,//SellingItem.quantity,
                    selectedItem = SellingItem,
                    itemTotal =  SellingItem.price,
                    productTitle = SellingItem.title,
                    removeListener = {cartVM.removeItem(SellingItem)},
                    addListener = {cartVM.addItem(SellingItem)},
                    reduceListener = {cartVM.reduceItem(SellingItem)}
                )
            }
        }

    }

}

@Composable
fun PrevEachItem(){
    ShoppingTheme {
        CartEachItem()
    }
}
@Composable
fun CartEachItem(
    modifier:Modifier = Modifier,
    painter: Painter = painterResource(R.drawable.coffee_animation),
    selectedItem: SellingItem? = null,
    curQuantity:Int = 1,
    itemTotal:Double = 6.99,
    itemTotalTextSize: TextUnit = 28.sp,
    productTitle:String = "I am the title",
    removeListener: () -> Unit = {},
    addListener:() -> Unit ={},
    reduceListener:()->Unit={}
){

    Row(
        modifier = modifier
            .background(color=Color.White)
            .defaultMinSize(minHeight = 100.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){

        //Selected Item Image Section
        Box(
            modifier = Modifier.weight(4.5f).padding(start = 8.dp)
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
                            modifier = Modifier.border(
                                width = 2.dp,
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.onTertiary
                            ).clip(shape = RoundedCornerShape(8.dp)),
                            model = selectedItem.imageUrl,
                            contentDescription = selectedItem.description?: "Cart Item Image",
                            placeholder = painterResource(R.drawable.coffee_animation),
                        )
                    } ?:
                    Image(
                        modifier = Modifier.border(
                            width = 2.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.onTertiary
                        ).clip(shape = RoundedCornerShape(8.dp)),
                        painter = painter,
                        contentDescription = "Cart Item Image",
                        //contentScale = ContentScale.Fit

                    )
                }
                Text(
                    //modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center,
                    text = productTitle
                )
            }
        }

        //Counting Section
        Box(
            modifier = Modifier.weight(2.5f),
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
                        fontSize = 18.sp
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
                        fontSize = 12.sp,
                        color = Color.Blue
                    )
                }
            }
        }
        val itemPrice = remember{
            selectedItem?.let{
                it.price.times(it.quantity)
            }
        }
        //Calculation Section/Price Section
        Box(
            modifier = Modifier.weight(2f),
            contentAlignment = Alignment.BottomCenter
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$itemPrice",
                    fontSize = itemTotalTextSize
                )
            }
        }
    }

}
