package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellingItem(
    var id:Int = 0,
    var image: Int = 0,
    var imageUrl:String = "",
    var title:String = "",
    var description:String = "null ",
    var price:Double = 0.00,
    var quantity:Int = 0
):Parcelable{

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            title,
            "${title.first()} ${title.last()}",
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

