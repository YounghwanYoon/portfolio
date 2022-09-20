package com.example.demoapp.repository.local.shopping

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDao {

    //Coroutine Live Data
    @Query("SELECT * FROM shopping_items")
    suspend fun getShoppingItems(): List<ShoppingItem_Local>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem_Local)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem_Local)

    //LiveData
    @Query("SELECT * FROM shopping_items")
    fun observeAllShoppingItems(): LiveData<List<ShoppingItem_Local>>

    @Query("SELECT SUM(price * amount) FROM shopping_items")
    fun observeTotalPrice():LiveData<Float>

}