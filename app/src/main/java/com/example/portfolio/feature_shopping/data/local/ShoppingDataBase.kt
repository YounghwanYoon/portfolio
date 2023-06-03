package com.example.portfolio.feature_shopping.data.local

import androidx.compose.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.portfolio.feature_shopping.data.local.entities.CartEntity
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.example.portfolio.feature_shopping.data.local.entities.SpecialItemEntity
import com.example.portfolio.feature_shopping.data.local.entities.UserEntity

@Database(
    entities = [ CartEntity::class, UserEntity::class, SpecialItemEntity::class, SellingItemEntity::class,],
    version = 1,
)
abstract class ShoppingDataBase :RoomDatabase(){

    abstract fun cartDao():CartDao
    abstract fun sellingItemDao():SellingItemDao
    abstract fun specialItemDao():SpecialItemDao

    companion object{
        const val CARTDB_NAME = "shopping_cart_db"
        //Prevent race condition
        @Volatile
        private var INSTANCE:ShoppingDataBase? = null

        //Execute by single thread //singleton
        //which prevent creating multiple Database.
        fun getInstance(context:Context):ShoppingDataBase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingDataBase::class.java,
                    "cart_db"
                ).build().also{
                    INSTANCE = it
                }
            }
        }
    }

}