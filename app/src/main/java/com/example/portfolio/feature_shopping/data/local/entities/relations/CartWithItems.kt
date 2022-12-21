package com.example.portfolio.feature_shopping.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.portfolio.feature_shopping.data.local.entities.CartEntity
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity

//One to Many for this app
//If it was from server,
// then many to many which requires cross reference and adding Join/Junction() to relation
data class CartWithItems (
    @Embedded val cartEntity: CartEntity,

    @Relation(
        parentColumn = "cartId",
        entityColumn = "cartId",
    )
    val sellingItems: List<SellingItemEntity>

)