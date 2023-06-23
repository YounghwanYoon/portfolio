package com.example.portfolio.feature_shopping.presentation.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.PaymentInfo
import com.example.portfolio.feature_shopping.domain.model.User
import com.example.portfolio.feature_shopping.presentation.utils.ConfirmNotificationService
import com.example.portfolio.feature_shopping.presentation.utils.CustomViewPager
import com.example.portfolio.feature_shopping.presentation.utils.Helper
import com.example.portfolio.feature_shopping.presentation.utils.Helper.nonScaledSp
import com.example.portfolio.feature_shopping.presentation.utils.MyDivider
import com.example.portfolio.feature_shopping.presentation.utils.ShoppingColors
import java.util.Locale

//https://stackoverflow.com/questions/68852110/show-custom-alert-dialog-in-jetpack-compose
@Preview
@Composable
fun PaymentDialogScreen(
    shouldOpenDialog: Boolean = true,
    title:String = stringResource(R.string.payment_diaglog_title),
    onDismissedCalled: () -> Unit = {},
    cartState : Cart = Cart(),
    //paymentViewModel: PaymentViewModel = hiltViewModel(),
    removeItems: () ->Unit = {},
    onComplete: () ->Unit = {},
    confirmNotificationService:ConfirmNotificationService = ConfirmNotificationService(LocalContext.current),
    isTablet:Boolean = true
){
    var openDialog by remember{ mutableStateOf(shouldOpenDialog)}
    var shouldChangeButtonColor by remember{ mutableStateOf(false) }
    val isTablet by remember{ mutableStateOf(isTablet)}

    if(openDialog){
        if(!isTablet){
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
                        removeItems()
                        onComplete()
                        openDialog= false
                        confirmNotificationService.showNotification(123456)
                    },
                    bodyContent = {
                        CustomViewPager(
                            contentList = listOf(
                                {ShippingAndPayment_Page(
                                    cartState = cartState
                                )},
                                {
                                    OrderSummary_Page(
                                        cartUIState = cartState,
                                    )
                                }
                            ),
                            onLastPage = {
                                shouldChangeButtonColor = true
                            }
                        )
                    }
                )
            }
        }
        else {
            Column(
                modifier= Modifier.verticalScroll(state = rememberScrollState())
            ) {

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
                            removeItems()
                            onComplete()
                            openDialog= false
                            confirmNotificationService.showNotification(123456)
                        },
                        bodyContent = {
                            CustomViewPager(
                                contentList = listOf(
                                    {ShippingAndPayment_Page(
                                        cartState = cartState
                                    )},
                                    {
                                        OrderSummary_Page(
                                            cartUIState = cartState,
                                        )
                                    }
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

    }
}
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

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun ShippingAndPayment_Page(
    modifier:Modifier = Modifier,
    user: User = User(),
    cartState: Cart = Cart(),
    paymentInfos:List<PaymentInfo> = user.paymentInfo,
    title:String = stringResource(R.string.payment_summary)
){
    var isRotatedOrTablet:Boolean by remember{mutableStateOf(true)}

    isRotatedOrTablet = when(Helper.isRoated()){
        true->{
            true
        }
        false -> {
            booleanResource(R.bool.is_tablet)
        }
    }

    when(isRotatedOrTablet){
        true -> {
            Column{
                TitleSection(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    title = title
                )
                Row{
                    Column(modifier = Modifier.weight(0.50f).fillMaxHeight()){
                        ShippingAddress(modifier = Modifier.weight(0.60f),user = user)

                        PaymentMethods(modifier = Modifier.weight(0.30f),paymentInfos = paymentInfos)
                    }
                    Spacer(modifier = Modifier.weight(0.10f))
                    Column(modifier = Modifier.weight(0.40f).fillMaxHeight()){
                        TotalPrice(modifier = Modifier.fillMaxSize(),givenSubTotal = cartState.subTotal)
                    }

                }
            }

        }
        false -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier=Modifier.background(color = MaterialTheme.colorScheme.background)
            ){
                TitleSection(modifier = Modifier.weight(0.05f), title = title)
                ShippingAddress(modifier = Modifier.weight(0.20f),user = user)
                PaymentMethods(modifier = Modifier.weight(0.15f),paymentInfos = paymentInfos)
                TotalPrice(modifier = Modifier.weight(0.30f),givenSubTotal = cartState.subTotal)
            }
        }
    }
}


@Composable
fun OrderSummary_Page(
    modifier:Modifier = Modifier,
    cartUIState:Cart = Cart(),
    title:String = "Order Summary",
){

    val cartItems by remember {
        mutableStateOf(cartUIState.items.toList())
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
        Text("Total Quantity - ${cartUIState.totalQuantity}")
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
    paymentInfos: List<PaymentInfo>,
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
                            .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp))
                    ){
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
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

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun TotalPrice(modifier:Modifier = Modifier.fillMaxWidth(), givenSubTotal:String = "7.99"){
    val subTotal by remember{ mutableStateOf(givenSubTotal) }
    val tax by remember{mutableStateOf(Helper.formatHelper(0.0825 * subTotal.toDouble()))}
    val shipping by remember{if(subTotal.toDouble() < 35.00) mutableStateOf(4.99) else mutableStateOf(0.00) }
    val total = remember{mutableStateOf(Helper.formatHelper(subTotal.toDouble() + tax + shipping)).value}

    var isRotatedOrTablet:Boolean by remember{mutableStateOf(false)}
    isRotatedOrTablet = when(Helper.isRoated()){
        true->{
            true
        }
        false -> {
            booleanResource(R.bool.is_tablet)
        }
    }

    when(isRotatedOrTablet){
        true -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ){
                MyDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .align(Alignment.CenterHorizontally)
                        .background(color = Color.Transparent),
                    height = 2.dp
                )
                Spacer(modifier=Modifier.padding(4.dp))
                Row(Modifier.fillMaxWidth(0.85f), verticalAlignment = Alignment.CenterVertically){
                    Spacer(Modifier.weight(0.05f))

                    Box(Modifier.weight(0.4f), contentAlignment = Alignment.TopStart){
                        Text("subTotal:")
                    }
                    Spacer(Modifier.weight(0.1f))

                    Box(Modifier.weight(0.45f), contentAlignment = Alignment.TopStart){
                        Text("$${subTotal}")
                    }
                }
                Row(Modifier.fillMaxWidth(0.85f), verticalAlignment = Alignment.CenterVertically){
                    Spacer(Modifier.weight(0.05f))

                    Box(Modifier.weight(0.4f), contentAlignment = Alignment.TopStart){
                        Text("tax:")
                    }
                    Spacer(Modifier.weight(0.1f))

                    Box(Modifier.weight(0.45f), contentAlignment = Alignment.TopStart){
                        Text("$$tax")
                    }
                }
                Row(Modifier.fillMaxWidth(0.85f), verticalAlignment = Alignment.CenterVertically){
                    Spacer(Modifier.weight(0.05f))
                    Box(Modifier.weight(0.4f), contentAlignment = Alignment.TopStart){
                        Text("shipping:")
                    }
                    Spacer(Modifier.weight(0.1f))
                    Box(Modifier.weight(0.45f), contentAlignment = Alignment.TopStart){
                        Text("$$shipping")
                    }
                }
                //Spacer(modifier=Modifier.padding(4.dp))
                MyDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .align(Alignment.CenterHorizontally)
                        .background(color = Color.Transparent),
                    height = 2.dp
                )
                Spacer(modifier=Modifier.padding(4.dp))
                Row(Modifier.fillMaxWidth(0.85f), verticalAlignment = Alignment.CenterVertically){
                    Spacer(Modifier.weight(0.05f))
                    Box(Modifier.weight(0.4f), contentAlignment = Alignment.TopStart){
                        Text("Total:")
                    }
                    Spacer(Modifier.weight(0.1f))
                    Box(Modifier.weight(0.45f), contentAlignment = Alignment.TopStart){
                        Text("$$total")
                    }
                }
            }
        }
        false ->{
            Column(
                modifier = modifier,
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
                Row(Modifier.fillMaxWidth(0.85f), verticalAlignment = Alignment.CenterVertically){
                    Box(Modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                        Text("subTotal:")
                    }
                    Spacer(Modifier.weight(0.1f))

                    Box(Modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                        Text("$${subTotal}")
                    }
                }
                Row(Modifier.fillMaxWidth(0.85f), verticalAlignment = Alignment.CenterVertically){
                    Box(Modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                        Text("tax:")
                    }
                    Spacer(Modifier.weight(0.1f))

                    Box(Modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                        Text("$$tax")
                    }
                }
                Row(Modifier.fillMaxWidth(0.85f), verticalAlignment = Alignment.CenterVertically){
                    Box(Modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                        Text("shipping:")
                    }
                    Spacer(Modifier.weight(0.1f))
                    Box(Modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
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
                Row(Modifier.fillMaxWidth(0.85f), verticalAlignment = Alignment.CenterVertically){
                    Box(Modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                        Text("Total:")
                    }
                    Spacer(Modifier.weight(0.1f))

                    Box(Modifier.weight(0.5f), contentAlignment = Alignment.TopStart){
                        Text("$$total")
                    }
                }
            }
        }
    }

}