package com.example.portfolio.feature_shopping.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartEntity(

    @PrimaryKey(autoGenerate = true)
    val cartId:Long,
    val cartOwnerId:Long,

)