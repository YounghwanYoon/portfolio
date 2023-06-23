package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellingItem(
    var id:Int = 0,
    var image: Int = 0,
    var imageUrl:String? = null,
    var title:String = "",
    var description:String = "null ",
    var price:Double = 0.00,
    var supplyQty:Int = 0,
    var quantityInCart:Int = 0,
    var itemTotal:Double = price.times(quantityInCart)
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

