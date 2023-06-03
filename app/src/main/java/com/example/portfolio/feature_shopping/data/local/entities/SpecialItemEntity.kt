package com.example.portfolio.feature_shopping.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "special_items")
data class SpecialItemEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var image:Int = 0,
    var imageUrl:String = "",
    var description:String = "null",
    var title:String,
    var start_date:String,
    var end_date:String,
)
