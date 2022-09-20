package com.example.demoapp.repository.local.shopping

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItem_Local::class], version = 1)
abstract class ShoppingDataBase ():RoomDatabase() {

    companion object{
        val DATABASE_NAME = "Shopping_db"
    }

    abstract fun getShoppingDao():ShoppingDao


}