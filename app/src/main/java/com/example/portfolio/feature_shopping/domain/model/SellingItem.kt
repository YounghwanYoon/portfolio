package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellingItem(
    var id:Int,
    var image: Int = 0,
    var imageUrl:String = "",
    var title:String,
    var description:String = "null ",
    var price:Double,
    var quantity:Int = 99
):Parcelable{

}

