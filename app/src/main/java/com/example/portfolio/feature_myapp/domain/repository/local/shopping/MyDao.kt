package com.example.demoapp.repository.local.shopping

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MyDao {

    //Coroutine Live Data
    @Query("SELECT * FROM shopping_items_local")
    suspend fun getShoppingItems(): List<ShoppingItem_Local>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem_Local)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem_Local)

    //LiveData
    @Query("SELECT * FROM shopping_items_local")
    fun observeAllShoppingItems(): LiveData<List<ShoppingItem_Local>>

    @Query("SELECT SUM(price * amount) FROM shopping_items_local")
    fun observeTotalPrice():LiveData<Float>

}