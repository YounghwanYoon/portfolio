package com.example.portfolio.feature_shopping.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.portfolio.feature_shopping.domain.model.SellingItem

@Entity
data class CartEntity(

    @PrimaryKey(autoGenerate = true)
    val cartId:Long,
    val cartOwnerId:Long,

)