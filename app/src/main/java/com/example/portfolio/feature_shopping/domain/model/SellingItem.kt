package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

@Parcelize
data class SellingItem(
    var id:Int = 0,
    var image: Int = 0,
    var imageUrl:String? = null,
    var title:String = "",
    var description:String = "null ",
    var price:Double = (Random.nextInt(4, 10) + 0.99),
    var supplyQty:Int = (Random.nextInt(3, 99)),
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

