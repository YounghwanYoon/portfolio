package com.example.portfolio.feature_shopping.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import kotlin.random.Random

@Entity (tableName = "selling_items")
data class SellingItemEntity(
    @PrimaryKey(autoGenerate = false)
    var itemId:Int,
    var cartId:Long?,
    //for compose test use only
    var image: Int = 0,
    var imageUrl:String?,
    var title:String?,
    var description:String = "null ",
    var price:Double,
    var quantity:Int = 99,
    val page:Int? = null

){
    fun toSellingItem():SellingItem{
        return SellingItem(
            id= this.itemId,
            image = 0,
            imageUrl = this.imageUrl,
            title = this.title!!,
            description = this.description,
            price = this.price,
            supplyQty = this.quantity
        )
    }
}