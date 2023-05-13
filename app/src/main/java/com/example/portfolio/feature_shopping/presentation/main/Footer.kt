
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.presentation.utils.Screens
import com.example.portfolio.feature_shopping.presentation.utils.ShoppingColors

@Preview
@Composable
fun Footer(
    modifier: Modifier = Modifier,
    navController: NavController = NavController(LocalContext.current),
    cartState: Cart = Cart(),
    //totalQuantity: Int = 0,
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp)
                .fillMaxSize()
                .graphicsLayer {
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                    )
                    clip = true
                }
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onTertiary,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                    )
                )
            /*.clip(shape = RoundedCornerShape(2.dp))*/,
        ) {
            val (home, cart, divider, counter) = createRefs()

            Icon(
                painter = painterResource(R.drawable.coffee_home_btn),
                contentDescription = null,
                modifier = Modifier
                    .background(Color.Transparent)
                    //.weight(0.50f)
                    .defaultMinSize(minHeight = 35.dp)
                    .clickable {
                        navController.navigate(Screens.Main.rout) {
                            popUpTo(Screens.Main.rout)
                        }
                    }.constrainAs(home){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(divider.start)
                    }
            )
            Divider(
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .constrainAs(divider){
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.size(1.dp)

            )

            Icon(
                painter = painterResource(R.drawable.coffee_cart_btn),
                contentDescription = null,
                modifier = Modifier
                    .background(Color.Transparent)
                    //.weight(0.50f)
                    .defaultMinSize(minHeight = 40.dp)
                    .clickable {
                        navController.navigate(Screens.Cart.rout) {
                            popUpTo(Screens.Cart.rout)
                        }
                    }.constrainAs(cart){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(divider.end)
                        end.linkTo(parent.end)
                    }
            )

            Box(
                modifier = Modifier.constrainAs(counter){
                    top.linkTo(cart.top, margin = 4.dp)
                    bottom.linkTo(cart.bottom)
                    start.linkTo(cart.start)
                    end.linkTo(cart.end)
                }
            ){
                Text(
                    text = "${cartState.totalQuantity}",//"$totalQuantity",
                    color = ShoppingColors.Brown_300
                )
            }





            /*fastImageButton(
                modifier = Modifier.weight(.33f),
                onClick = {
                    navController
                    println("First Image Clicked TO THE PROFILE")
                },
                painter = painterResource(R.drawable.coffee_menu_btn)
            )
            fastImageButton(
                modifier = Modifier.weight(.33f),
                onClick = {
                    navController.navigate(Screens.Main.rout) {
                        popUpTo(Screens.Main.rout)
                    }
                    println("First Image Clicked TO THE HOME")

                },
                painter = painterResource(R.drawable.coffee_home_btn)
            )
            fastImageButton(
                modifier = Modifier
                    .weight(.33f),
                    //.defaultMinSize(50.dp),
                onClick = {
                    navController.navigate(Screens.Cart.rout) {
                        popUpTo(Screens.Cart.rout)
                    }
                    println("First Image Clicked TO THE CART!!")

                },
                painter = painterResource(R.drawable.coffee_cart_btn),
            ) {
                when {
                    totalQuantity > 99 -> {
                        Text(
                            text = " ... ",
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            ),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    totalQuantity == 0 ->{

                    }
                    else -> {
                        Text(
                            text = "$totalQuantity",
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            ),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }*/
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun fastImageButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    painter: Painter = painterResource(R.drawable.coffee_home_btn),
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clickable { },
        contentAlignment = Alignment.Center,

        ) {
        ConstraintLayout(/*modifier = Modifier.padding(top = 18.dp)*/) {

            val (IconBtn, Counter) = createRefs()

            Box(modifier = Modifier
                .constrainAs(Counter) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    absoluteRight.linkTo(parent.absoluteRight)
                    width = Dimension.fillToConstraints
                }
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
                Icon(
                    painter = painter,
                    contentDescription = "Coffee Menu Button"
                )
            }
        }
    }
}
