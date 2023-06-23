package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpecialItem(
    var id:Int,
    var cartId:Long?=null,
    var image:Int = 0,
    var imageUrl:String = "",
    var description:String = "null",
    var title:String,
    var start_date:String,
    var end_date:String,
):Parcelable
