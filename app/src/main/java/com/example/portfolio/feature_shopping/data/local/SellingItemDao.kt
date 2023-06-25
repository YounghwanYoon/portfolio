package com.example.portfolio.feature_shopping.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SellingItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: SellingItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items:List<SellingItemEntity>)

    @Transaction
    @Query("Select * FROM selling_items WHERE cartId = :cartId")
    suspend fun getUserCartItems(cartId:Long):List<SellingItemEntity>

    //@Transaction
    @Query("SELECT * FROM selling_items WHERE itemId = :id")
    suspend fun getSelectedItem(id:Int): SellingItemEntity


    @Transaction
    @Query("Select * FROM selling_items")
    suspend fun getSellingItems():List<SellingItemEntity>

    @Query("Select * From selling_items")
    fun getSellingItemSource(): PagingSource<Int, SellingItemEntity>

    @Delete
    suspend fun deleteSellingItem(sellingItemEntity: SellingItemEntity)

    @Query("Delete From selling_items")
    suspend fun clearAll()

}