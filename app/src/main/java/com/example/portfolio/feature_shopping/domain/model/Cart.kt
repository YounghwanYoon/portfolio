package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable
import androidx.compose.runtime.mutableStateMapOf
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    //Items vs Quantity
    //compose will not listen to change for mutableMapOf but mutableStateMapOf and it applies to other lists
    val items: MutableMap<SellingItem, Int> = mutableStateMapOf<SellingItem, Int>(),
    var totalQuantity:Int = 0,
    var subTotal:Double = 0.0
):Parcelable
