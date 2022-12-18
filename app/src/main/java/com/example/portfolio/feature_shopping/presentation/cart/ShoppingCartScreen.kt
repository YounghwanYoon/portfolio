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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.presentation.detail.ImageFrame
import com.example.portfolio.feature_shopping.presentation.ui.theme.Brown_300
import com.example.portfolio.feature_shopping.presentation.ui.theme.ShoppingTheme

@Composable
fun ShoppingCartScreen(modifier:Modifier = Modifier, navController:NavController){
    ShoppingTheme{
        Column(){
            ShoppingCartBody(Modifier.weight(9f))
            Footer(Modifier.weight(1f), navController = navController)
        }
    }

}

@Composable
fun ShoppingCartHeader(modifier:Modifier = Modifier){
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun ShoppingCartBody(modifier:Modifier = Modifier){

    Surface(
        modifier = modifier,
        shadowElevation = 8.dp
    ){

        //In order to stay top of ImageFrame, it need to be called after ImageFrame
        ConstraintLayout() {
            val (OrderBtn, ListOrder) = createRefs()

            ImageFrame(
                modifier = Modifier.fillMaxHeight(),
/*                modifier = Modifier.constrainAs(ListOrder){
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    top.linkTo(parent.top, margin = 8.dp)
                    //top.linkTo(OrderBtn.top)
                    bottom.linkTo(parent.bottom, margin= 8.dp)
                },*/
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
                        CartListAndSubTotal()
                    }

                }
            ){
                //This is place for subtotal
                Text("Sub Total")
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


data class CartItem(
    val id:Int,
    var itemTitle:String = "I am the title",
    var image:Painter,
    var count:Int,
    var price: Double,
)

//@Preview(widthDp = 360, heightDp = 640)
@Composable
fun CartListAndSubTotal(
    modifier:Modifier = Modifier,
    listCartItems:List<CartItem> = listOf(
        CartItem(id = 0, count = 1, price = 1.99, itemTitle = "Korea Coffee", image = painterResource(R.drawable.coffee_animation),),
        CartItem(id = 1, count = 1, price = 2.99, itemTitle = "America Coffee", image = painterResource(R.drawable.coffee_animation),),
        CartItem(id = 2, count = 1, price = 3.99, itemTitle = "Brazil Coffee", image = painterResource(R.drawable.coffee_animation),),
        CartItem(id = 3, count = 1, price = 4.99, itemTitle = "Mexico Coffee", image = painterResource(R.drawable.coffee_animation),),
        CartItem(id = 4, count = 1, price = 5.99, itemTitle = "England Coffee", image = painterResource(R.drawable.coffee_animation),),
        CartItem(id = 5, count = 1, price = 6.99, itemTitle = "Italia Coffee", image = painterResource(R.drawable.coffee_animation),),
    )

){
    Box(
        modifier = modifier ,
    ){
        LazyColumn{
            items(listCartItems){ item ->
                CartEachItem(
                    modifier = Modifier.padding(bottom = 16.dp),
                    counter = item.count,
                    id = item.id,
                    painter = item.image,
                    itemTotal = (item.count.times(item.price)),
                    productTitle = item.itemTitle,
                    removeListener = {}
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
    id:Int = 0,
    counter:Int = 1,
    itemTotal:Double = 6.99,
    itemTotalTextSize: TextUnit = 28.sp,
    productTitle:String = "I am the title",
    removeListener: () -> Unit = {}
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
                    IconButton(
                        onClick = {},
                        enabled = false,
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
                        text = counter.toString(),
                        fontStyle = FontStyle.Italic,
                        fontSize = 18.sp
                    )
                    IconButton(
                        onClick = {},
                        enabled = false,
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
                    onClick = {
                              //ToDo Update/Remove item from CartList
                    },
                ){
                    Text(
                        text ="remove",
                        fontSize = 12.sp,
                        color = Color.Blue
                    )
                }
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
                    text = "$${itemTotal}",
                    fontSize = itemTotalTextSize
                )
            }
        }
    }

}

@Composable
fun ShoppingCartFooter(modifier:Modifier = Modifier){

}

