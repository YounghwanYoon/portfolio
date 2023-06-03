package com.example.portfolio.feature_shopping.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "selling_items")
data class SellingItemEntity(
    @PrimaryKey(autoGenerate = false)
    var itemId:Int,
    var cartId:Long?,

    //for compose test use only
    var image: Int = 0,
    var imageUrl:String = "",
    var title:String,
    var description:String = "null ",
    var price:Double,
    var quantity:Int = 99

){}