package com.example.portfolio.feature_shopping.data.local

import androidx.compose.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.portfolio.feature_shopping.data.local.entities.CartEntity
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.example.portfolio.feature_shopping.data.local.entities.SpecialItemEntity
import com.example.portfolio.feature_shopping.data.local.entities.UserEntity
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.SpecialItem

@Database(
    entities = [
        UserEntity::class,
        SpecialItemEntity::class,
        SellingItemEntity::class,
        CartEntity::class,
    ],
    version = 1,
)
abstract class CartDataBase :RoomDatabase(){
    abstract fun CartDao():CartDao

    companion object{
        //Prevent race condition
        @Volatile
        private var INSTANCE:CartDataBase? = null

        //Execute by single thread //singleton
        //which prevent creating multiple Database.
        fun getInstance(context:Context):CartDataBase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CartDataBase::class.java,
                    "cart_db"
                ).build().also{
                    INSTANCE = it
                }
            }
        }
    }

}