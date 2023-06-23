package com.example.portfolio.feature_shopping.data.local

import androidx.room.*
import com.example.portfolio.feature_shopping.data.local.entities.CartEntity
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.example.portfolio.feature_shopping.data.local.entities.UserEntity
import com.example.portfolio.feature_shopping.data.local.entities.relations.CartWithItems


@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity)

    @Transaction
    @Query("Select * From CartEntity where cartId = :cartId")
    suspend fun getCartWithItems(
        //common item
        cartId:Long
    ):List<CartWithItems>


}