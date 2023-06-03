package com.example.portfolio.feature_shopping.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.portfolio.feature_shopping.data.local.entities.CartEntity
import com.example.portfolio.feature_shopping.data.local.entities.UserEntity


//One to One relationship
//One user has only one cart and vice versa.
data class CartAndUser(
    @Embedded val cartEntity: CartEntity,

    @Relation(
        parentColumn = "cartOwnerId",
        entityColumn = "userId"
    )
    @Embedded val userEntity: UserEntity,
)
