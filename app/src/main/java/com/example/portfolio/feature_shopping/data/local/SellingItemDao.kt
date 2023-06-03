package com.example.portfolio.feature_shopping.data.local

import androidx.room.*
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity

@Dao
interface SellingItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: SellingItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items:List<SellingItemEntity>)

    @Transaction
    @Query("Select * FROM selling_items WHERE cartId = :cartId")
    suspend fun getUserCartItems(cartId:Long):List<SellingItemEntity>

    @Transaction
    @Query("SELECT * FROM selling_items WHERE itemId = :itemId")
    suspend fun getSelectedItem(itemId:Int):SellingItemEntity


    @Transaction
    @Query("Select * FROM selling_items")
    suspend fun getSellingItems():List<SellingItemEntity>


    @Delete
    suspend fun deleteSellingItem(sellingItemEntity: SellingItemEntity)

}