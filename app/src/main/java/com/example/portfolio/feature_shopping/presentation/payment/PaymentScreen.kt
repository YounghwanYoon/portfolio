package com.example.portfolio.feature_shopping.presentation.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.PaymentInfo
import com.example.portfolio.feature_shopping.domain.model.User
import com.example.portfolio.feature_shopping.presentation.main.MyDivider
import com.example.portfolio.feature_shopping.presentation.utils.Helper
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
/*    Card(
        modifier = modifier
            .padding(horizontal =10.dp, vertical =5.dp)
            .fillMaxSize()
            .background(color = ShoppingColors.Brown_50)
        //.align(Alignment.Center)
        ,
        shape = RoundedCornerShape(10.dp),
    ){*/
    Column{
        Row(
            modifier= Modifier.weight(0.9f)
                .fillMaxWidth()
        ){
            bodyContent()
        }

        Row(
            modifier= Modifier.weight(0.1f)
                .fillMaxWidth()
                .background(
                    color = if(!onCompleted.value) ShoppingColors.LightGrey else ShoppingColors.Brown_300),
            horizontalArrangement = Arrangement.Center
        ){
            TextButton(
                modifier = Modifier.fillMaxSize(),
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
    //}
}
/*
@Composable
fun PaymentUIDialog(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean> = mutableStateOf(false),
    onCompleted:MutableState<Boolean> = mutableStateOf(false),
    bodyContent:@Composable () -> Unit,

){
    Card(
        modifier = modifier
            .padding(horizontal =10.dp, vertical =5.dp)
            .fillMaxSize()
            .background(color = Color.Transparent)
        //.align(Alignment.Center)
        ,
        shape = RoundedCornerShape(10.dp),
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Column(
                modifier = Modifier.weight(0.1f). padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp),
                    text = "Title Section"
                )
                MyDivider(
                    modifier = Modifier.fillMaxWidth(0.85f)
                        .align(Alignment.CenterHorizontally)
                        .background(color = Color.Transparent),
                    height = 2.dp
                )
            }

            Row(
                modifier= Modifier.weight(0.8f)
                    .fillMaxWidth()
            ){
                bodyContent()
            }

            Row(
                modifier= Modifier.weight(0.1f)
                    .fillMaxWidth()
                    .background(
                        color = if(!onCompleted.value) ShoppingColors.LightGrey else ShoppingColors.Brown_50),
                horizontalArrangement = Arrangement.Center
            ){
                TextButton(
                    modifier = Modifier.fillMaxSize(),
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
}*/

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
    ){
        TitleSection(modifier = Modifier.weight(0.05f), title = title)
        ShippingAddress(modifier = Modifier.weight(0.25f),user = user)
        PaymentMethods(modifier = Modifier.weight(0.15f),paymentInfos = paymentInfos)
        TotalPrice(modifier = Modifier.weight(0.45f),givenSubTotal = cart.subTotal)
/*        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ){
            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                modifier = Modifier.weight(0.5f),
                text = "subTotal"
            )
            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                modifier = Modifier.weight(0.5f).padding(start = 16.dp),
                text = "$0.00"
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ){
            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                modifier = Modifier.weight(0.5f),
                text = "tax"
            )
            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                modifier = Modifier.weight(0.5f).padding(start = 16.dp),
                text = "$0.00"
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ){
            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                modifier = Modifier.weight(0.5f),
                text = "shipping"
            )
            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                modifier = Modifier.weight(0.5f).padding(start = 16.dp),
                text = "$0.00"
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))
        MyDivider(modifier = Modifier.fillMaxWidth(0.8f).background(color = Color.LightGray))
        Spacer(modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ){
            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                modifier = Modifier.weight(0.5f),
                text = "Total"
            )
            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                modifier = Modifier.weight(0.5f).padding(start = 16.dp),
                text = "$0.00"
            )
        }*/
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
                .padding(vertical = 8.dp),
            text = title
        )
        Spacer(modifier = Modifier.padding(8.dp))
        MyDivider(
            modifier = Modifier.fillMaxWidth(0.85f)
                .align(Alignment.CenterHorizontally)
                /*.background(color = Color.Transparent)*/,
            height = 2.dp
        )
    }
}
@Composable
fun ShippingAddress(modifier:Modifier = Modifier, user:User = User()){
    Column(modifier = modifier){
        Text(modifier = Modifier.padding(8.dp), text = "Address")
        Card(modifier = Modifier.padding(8.dp).fillMaxWidth()){
            Column{
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically){
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Recipient: ${user.first_name} ${user.last_name}"
                    )
                    Spacer(modifier = Modifier.weight(1f))
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
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
        ){
            Text("Choose Payment Type")
            Spacer(modifier = Modifier.padding(8.dp).fillMaxWidth())
            LazyRow(horizontalArrangement = Arrangement.Center){
                items(paymentInfos.size){ index ->
                    Spacer(modifier = Modifier.padding(4.dp))

                    Card(
                        modifier = modifier
                            .defaultMinSize(minWidth = 150.dp, minHeight = 50.dp)
                            .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp)),
                    ){
                        Text(text = "xxx-${paymentInfos[index].lastFourDigit}")
                    }
                    Spacer(modifier = Modifier.padding(4.dp))

                }
                item(){
                    Card(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 150.dp, minHeight = 50.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
                    ){
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

    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceEvenly){
        Column(modifier = Modifier.padding(12.dp)){
            MyDivider(
                modifier = Modifier.fillMaxWidth(0.85f)
                    .align(Alignment.CenterHorizontally)
                    .background(color = Color.Transparent),
                height = 2.dp
            )
            Spacer(modifier=Modifier.padding(4.dp))
            Row(modifier.fillMaxWidth(0.8f)){
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("subTotal:")
                }
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("${subTotal}")
                }
            }
            Row(modifier.fillMaxWidth(0.8f)){
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("tax:")
                }
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("$tax")
                }
            }
            Row(modifier.fillMaxWidth(0.8f)){
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("shipping:")
                }
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("$$shipping")
                }
            }
            Spacer(modifier=Modifier.padding(4.dp))
            MyDivider(
                modifier = Modifier.fillMaxWidth(0.85f)
                    .align(Alignment.CenterHorizontally)
                    .background(color = Color.Transparent),
                height = 2.dp
            )
            Spacer(modifier=Modifier.padding(4.dp))
            Row(modifier.fillMaxWidth(0.8f)){
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("Total:")
                }
                Box(modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                    Text("$$total")
                }
            }
        }
    }
}