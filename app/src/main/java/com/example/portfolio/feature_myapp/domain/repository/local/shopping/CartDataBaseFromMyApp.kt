package com.example.portfolio.feature_myapp.domain.repository.local.shopping

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.demoapp.repository.local.shopping.MyDao
import com.example.demoapp.repository.local.shopping.ShoppingItem_Local
import com.example.portfolio.feature_myapp.domain.repository.local.myapp.MyAppDao
import com.example.portfolio.feature_shopping.data.local.CartDao

@Database(
    entities = [
        ShoppingItem_Local::class

     ], version = 1)

abstract class CartDataBaseFromMyApp:RoomDatabase() {

    companion object{
        val DATABASE_NAME = "shopping_db"
    }

    abstract fun getShoppingDao():MyDao

}