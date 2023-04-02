import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.presentation.utils.Screens

@Preview
@Composable
fun Footer(
    modifier: Modifier = Modifier,
    navController: NavController = NavController(LocalContext.current),
    totalQuantity:Int = 3,
) {

    Row(modifier = modifier){
        Row(
            modifier = Modifier
                //.padding(top=4.dp, bottom = 4.dp)
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onTertiary,
                    shape = RoundedCornerShape(2.dp)
                )
                .clip(shape = RoundedCornerShape(2.dp)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            fastImageButton(
                modifier = Modifier.weight(.3f),
                onClick = {
                    navController
                    println("First Image Clicked TO THE PROFILE")
                },
                painter = painterResource(R.drawable.coffee_menu_btn)
            )
            fastImageButton(
                modifier = Modifier.weight(.3f),
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
                    .weight(.3f)
                    .defaultMinSize(50.dp),
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
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun fastImageButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    painter: Painter,
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
                    bottom.linkTo(IconBtn.top)
                    absoluteRight.linkTo(parent.absoluteRight)
                    //absoluteLeft.linkTo(IconBtn.absoluteRight)


                    //linkTo( IconBtn.absoluteLeft, parent.absoluteRight, startMargin = 8.dp, bias = 1f)
                    width = Dimension.fillToConstraints
                }
                //.background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape),

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
