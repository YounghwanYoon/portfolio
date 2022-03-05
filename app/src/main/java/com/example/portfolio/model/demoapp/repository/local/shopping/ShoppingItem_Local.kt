package com.example.demoapp.repository.local.shopping

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "shopping_items")
data class ShoppingItem_Local(

    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    var imageUrl:String,
    var name:String,
    var price: Float,
    var amount: Int,
    ) {

}