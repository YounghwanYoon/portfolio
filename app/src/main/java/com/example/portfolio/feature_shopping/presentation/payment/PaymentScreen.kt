package com.example.portfolio.feature_shopping.presentation.payment

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.PaymentInfo
import com.example.portfolio.feature_shopping.domain.model.User
import com.example.portfolio.feature_shopping.presentation.ui.theme.ShoppingTheme
import com.example.portfolio.feature_shopping.presentation.utils.Helper
import com.example.portfolio.feature_shopping.presentation.utils.MyDivider
import com.example.portfolio.feature_shopping.presentation.utils.ShoppingColors
import com.example.portfolio.feature_shopping.presentation.utils.ViewPager
import java.util.*

@Preview(
    showSystemUi = false,
    showBackground = true,
    name = "Preview_PaymentScreen",
    group = "",
    widthDp = 360,
    heightDp = 640,
    locale = "us",
    backgroundColor = 0,
    uiMode = 0,
    device = ""
)
@Composable
fun PaymentScreen(
    modifier:Modifier=Modifier,
    //paymentVM:PaymentViewModel = hiltViewModel()
){
    CustomPaymentDialog()
    //ShippingAndPayment_Page()
}
//https://stackoverflow.com/questions/68852110/show-custom-alert-dialog-in-jetpack-compose
@Composable
fun CustomPaymentDialog(
    openDialogCustom: MutableState<Boolean> = mutableStateOf(true),
    title:String ="Order Summary"
){
    Dialog(
        onDismissRequest = {openDialogCustom.value = false}
    ){
        PaymentUIDialog(
            modifier =  Modifier.background(color = Color.White),
            openDialogCustom = openDialogCustom,
            bodyContent = {
                ViewPager(
                    contentList = listOf({ShippingAndPayment_Page()}, {OrderSummary_Page()})
                )
            }
        )
    }
}


@Composable
fun PaymentUIDialog(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean> = mutableStateOf(false),
    onCompleted:MutableState<Boolean> = mutableStateOf(false),
    bodyContent:@Composable () -> Unit,
){
    Column(modifier = modifier){
        //MainBody
        Row(
            modifier= Modifier.weight(0.95f)
                .fillMaxWidth()
                .background(color = Color.White)
        ){
            bodyContent()
        }

        //Bottom
        Row(
            modifier= Modifier//.weight(0.07f)
                .fillMaxWidth()
                .background(
                    color = if(!onCompleted.value) ShoppingColors.LightGrey else ShoppingColors.Brown_300,
                    shape = RoundedCornerShape(4.dp)
                ),
            horizontalArrangement = Arrangement.Center
        ){
            TextButton(
                modifier = Modifier,
                enabled = if(onCompleted.value == false) false else true,
                onClick = {onCompleted.value = false},
            ){
                Text(
                    text = "Complete Order",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize= 26.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ShippingAndPayment_Page(
    modifier:Modifier = Modifier,
    user: User = User(),
    cart: Cart = Cart(),
    paymentInfos:List<PaymentInfo> = user.paymentInfo,
    title:String = "Payment Summary"
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier=Modifier.background(color = MaterialTheme.colorScheme.background)
    ){
        TitleSection(modifier = Modifier.weight(0.05f), title = title)
        ShippingAddress(modifier = Modifier.weight(0.20f),user = user)
        PaymentMethods(modifier = Modifier.weight(0.15f),paymentInfos = paymentInfos)
        TotalPrice(modifier = Modifier.weight(0.30f),givenSubTotal = cart.subTotal)
    }

}
@Composable
fun OrderSummary_Page(modifier:Modifier = Modifier, user:User = User(), title:String = "Order Summary"){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        TitleSection(modifier = Modifier.weight(0.05f), title = title)
        Card(modifier.weight(0.90f)){
            Row(){
                Text("List of Items")
                Text("Item Quantity")
            }
        }
        MyDivider()
        Text("Total Quantity")
    }
}

@Composable
fun TitleSection(modifier:Modifier = Modifier, title:String = "Title Section"){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(vertical = 4.dp),
            fontStyle = FontStyle.Italic,
            text = title,
        )
        MyDivider(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                /*.background(color = Color.Transparent)*/,
            height = 2.dp
        )
    }
}

@Composable
fun ShippingAddress(modifier:Modifier = Modifier, user:User = User()){
    Column(modifier = modifier){
        Text(modifier = Modifier.padding(8.dp), text = "Shipping Address")
        Card(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth()){
            Column{
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically){
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "${user.first_name} ${user.last_name}"
                    )
                    Spacer(modifier = Modifier.weight(0.8f))// push below compose to right ("change" in this case)
                    TextButton(
                        onClick = {},
                    ){
                        Text(
                            text = "Change",
                            fontSize = 12.sp,
                            color = Color.Blue
                        )
                    }
                }
                Row{
                    Spacer(modifier = Modifier.padding(4.dp))

                    Text("Street: ${user.address_shipping}")
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Row{
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text("City: ${user.city}")

                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "State: ${user.state.uppercase(Locale.ROOT)}"
                    )
                    Spacer(modifier = Modifier.padding(8.dp))

                    Text(
                        text = "Zip: ${user.zip}"
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun PaymentMethods(
    modifier:Modifier = Modifier,
    paymentInfos: List<PaymentInfo> /*= mutableStateListOf(PaymentInfo())*/,
    addPayment: ()->Unit = {}
){
    Column(modifier = modifier){
        Column(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
        ){
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Choose Payment Type"
            )
            Spacer(modifier = Modifier.padding(8.dp).fillMaxWidth())
            LazyRow(horizontalArrangement = Arrangement.Center){
                items(paymentInfos.size){ index ->
                    Spacer(modifier = Modifier.padding(4.dp))

                    Card(
                        modifier = modifier
                            .defaultMinSize(minWidth = 150.dp, minHeight = 50.dp)
                            .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp)),
                    ){
                        Row(verticalAlignment = Alignment.CenterVertically ){
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(text = "xxx-${paymentInfos[index].lastFourDigit}")
                        }
                    }
                    Spacer(modifier = Modifier.padding(4.dp))

                }
                item(){
                    Card(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 150.dp, minHeight = 50.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
                    ){
                        Column(verticalArrangement = Arrangement.Center){
                            TextButton(
                                onClick = {}
                            )
                            {
                                Text(
                                    textAlign = TextAlign.Center,
                                    text ="Add +"
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(4.dp))

                }
                //Add Image
            }
        }

    }
}

@Composable
fun TotalPrice(modifier:Modifier = Modifier, givenSubTotal:Double = 7.99){
    val subTotal by remember{ mutableStateOf(givenSubTotal) }
    val tax by remember{mutableStateOf(Helper.formatHelper(0.9 * subTotal))}
    val shipping by remember{if(subTotal < 25.00) mutableStateOf(4.99) else mutableStateOf(0.00) }
    val total = remember{subTotal + tax + shipping}


    Column(modifier = modifier,){
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            MyDivider(
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .background(color = Color.Transparent),
                height = 2.dp
            )
            Spacer(modifier=Modifier.padding(4.dp))
            Row(modifier.fillMaxWidth(0.75f), verticalAlignment = Alignment.CenterVertically){
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("subTotal:")
                }
                Spacer(Modifier.weight(0.1f))

                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("$${subTotal}")
                }
            }
            Row(modifier.fillMaxWidth(0.75f), verticalAlignment = Alignment.CenterVertically){
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("tax:")
                }
                Spacer(Modifier.weight(0.1f))

                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("$$tax")
                }
            }
            Row(modifier.fillMaxWidth(0.75f), verticalAlignment = Alignment.CenterVertically){
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("shipping:")
                }
                Spacer(Modifier.weight(0.1f))
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("$$shipping")
                }
            }
            //Spacer(modifier=Modifier.padding(4.dp))
            MyDivider(
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .background(color = Color.Transparent),
                height = 2.dp
            )
            Spacer(modifier=Modifier.padding(4.dp))
            Row(modifier.fillMaxWidth(0.75f), verticalAlignment = Alignment.CenterVertically){
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("Total:")
                }
                Spacer(Modifier.weight(0.1f))

                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("$$total")
                }
            }
        }
    }
}