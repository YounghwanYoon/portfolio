package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    //Items vs Quantity
    val items: MutableMap<SellingItem, Int> = mutableMapOf(),
    var totalQuantity:Int = 0,
    var subTotal:Double = 0.0
):Parcelable
