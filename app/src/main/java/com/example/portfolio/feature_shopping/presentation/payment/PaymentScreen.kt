package com.example.portfolio.feature_shopping.presentation.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.PaymentInfo
import com.example.portfolio.feature_shopping.domain.model.User
import com.example.portfolio.feature_shopping.presentation.utils.*
import com.example.portfolio.feature_shopping.presentation.utils.Helper.nonScaledSp
import java.util.*

//https://stackoverflow.com/questions/68852110/show-custom-alert-dialog-in-jetpack-compose
@Composable
fun PaymentDialogScreen(
    shouldOpenDialog: Boolean,
    title:String ="Order Summary",
    onDismissedCalled: () -> Unit,
    cartData: Cart,
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    removeItems: () ->Unit,
    onComplete: () ->Unit,
    confirmNotificationService:ConfirmNotificationService = ConfirmNotificationService(LocalContext.current)
){
    var openDialog by remember{ mutableStateOf(shouldOpenDialog)}
    var shouldChangeButtonColor by remember{ mutableStateOf(false) }

    println("from Payment Dialog, openDialog $openDialog")
    println("shouldChnageButton Color $shouldChangeButtonColor")
    if(openDialog){
        CustomDialog(
            onDismissRequest = {
                openDialog = !openDialog
                onDismissedCalled()
            },
            width = 0.9f
        ){
            DialogContent(
                modifier =  Modifier.background(color = Color.White),
                changeButtonColor = if(shouldChangeButtonColor) true else false,
                onCompleted = {
                    //TODO::
                    // 1. Complete Order Summary Page by adding Cart List - completed but improvement is needed
                    // 2. Show Order Confirmation Page
                    // 3. Add Order History Page
                    // 4. Update CartList to Empty from CartViewModel. - completed
                    // Maybe try to pass savedstate from viewmodel to usecase. - failed

                    //paymentViewModel.removeAllItems()
                    //cartStateViewModel.removeAllItem()
                    removeItems()
                    onComplete()
                    openDialog= false
                    confirmNotificationService.showNotification(123456)
                },
                //openDialogCustom = openDialogState,
                bodyContent = {
                    CustomViewPager(
                        contentList = listOf(
                            {ShippingAndPayment_Page()},
                            {OrderSummary_Page(cartData = cartData)}
                        ),
                        onLastPage = {
                            shouldChangeButtonColor = true
                        }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    width:Float = 0.9f,
    content: @Composable () -> Unit,
) {
    println("CustomDialog  is called")
    Dialog(
        onDismissRequest = onDismissRequest,
        // We are copying the passed properties
        // then setting usePlatformDefaultWidth to false
        properties = properties.let {
            DialogProperties(
                dismissOnBackPress = it.dismissOnBackPress,
                dismissOnClickOutside = it.dismissOnClickOutside,
                securePolicy = it.securePolicy,
                usePlatformDefaultWidth = false
            )
        },
        content = {
            Surface(
                color = Color.Transparent,
                //this is where to control size of dialog
                modifier = Modifier.fillMaxWidth(width),
                content = content
            )
        }
    )
}

@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    //openDialogCustom: MutableState<Boolean> = mutableStateOf(false),
    changeButtonColor: Boolean,
    onCompleted: () -> Unit,
    bodyContent:@Composable () -> Unit,
){
    Column(modifier = modifier){
        //MainBody
        Row(
            modifier= Modifier
                .weight(0.95f)
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
                    color = if (!changeButtonColor) ShoppingColors.LightGrey else ShoppingColors.Brown_300,
                    //shape = RoundedCornerShape(4.dp)
                ),
            horizontalArrangement = Arrangement.Center
        ){
            TextButton(
                modifier = Modifier,
                enabled = changeButtonColor,
                onClick = {
                    println("Complete Order Btn is clicked")
                    onCompleted()
                    //onCompleted.value = false
                    //changeBtnColorState = false
                },
            ){
                Text(
                    text = "Complete Order",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize= 26.nonScaledSp,
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

@Preview(
    name = "OrderSummary",
    widthDp = 360,
    heightDp = 640,
)
@Composable
fun OrderSummary_Page(
    modifier:Modifier = Modifier,
    cartData:Cart = Cart(),
    title:String = "Order Summary",
){

    var cartItems by remember {
        mutableStateOf(cartData.items.toList())
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        TitleSection(modifier = Modifier.weight(0.05f), title = title)
        Card(
            modifier
                .weight(0.90f)
                .fillMaxWidth(0.90f)){
            LazyColumn(){
                items(cartItems){(sellingItem, quantity)->
                    Row(modifier = Modifier.padding(8.dp)){
                        Surface(
                            modifier = Modifier.weight(0.2f)
                            // color = Color.Black.copy(alpha=0.0f)
                        ){
                            sellingItem.let{
                                AsyncImage(
                                    modifier = Modifier
                                        .border(
                                            width = 2.dp,
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.onTertiary
                                        )
                                        .clip(shape = RoundedCornerShape(8.dp)),
                                    model = it.imageUrl,
                                    contentDescription = sellingItem.description?: "Cart Item Image",
                                    placeholder = painterResource(R.drawable.coffee_animation),
                                )
                            }

                        }
                        Spacer(modifier.weight(0.6f))
                        Text(
                            modifier = Modifier.weight(0.2f),
                            text = "Qty: $quantity"
                        )
                    }
                }
            }

        }
        MyDivider()
        Text("Total Quantity - ${cartData.totalQuantity}")
    }
}

@Composable
fun TitleSection(modifier:Modifier = Modifier, title:String = "Title Section"){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 4.dp),
            fontStyle = FontStyle.Italic,
            text = title,
        )
        MyDivider(
            modifier = Modifier
                .fillMaxWidth()
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
        Card(modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()){
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
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
        ){
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Choose Payment Type"
            )
            Spacer(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth())
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
                modifier = Modifier
                    .fillMaxWidth()
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
                modifier = Modifier
                    .fillMaxWidth()
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