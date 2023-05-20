package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Cart(
    //Items vs Quantity
    //compose will not listen to change for mutableMapOf but mutableStateMapOf and it applies to other lists
//    val items: MutableMap<SellingItem, Int> = mutableStateMapOf(),
    var items: MutableMap<SellingItem, Int> = mutableMapOf(),
    //var items:List<SellingItem> = mutableListOf(),
    var totalQuantity: Int = 0,
    var subTotal:String = "0.00",
    var activatePayment:Boolean = false,
):Parcelable{

}
