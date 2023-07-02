package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Cart(
    var items: MutableMap<SellingItem, Int> = mutableMapOf(),
    var totalQuantity: Int = 0,
    var subTotal:String = "0.00",
    var activatePayment:Boolean = false,
):Parcelable{

}
