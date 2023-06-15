package com.example.portfolio.feature_pagination.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.portfolio.feature_shopping.data.local.ShoppingDataBase


@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class MovieRoomDB : RoomDatabase(){

    abstract val movieDao:MovieDao

    companion object{
        const val MOVIE_DB_NAME = "movie_db"
        //Prevent race condition
        @Volatile
        private var INSTANCE: ShoppingDataBase? = null

        //Execute by single thread //singleton
        //which prevent creating multiple Database.
        fun getInstance(context: Context): ShoppingDataBase {
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingDataBase::class.java,
                    "movie_db"
                ).build().also{
                    INSTANCE = it
                }
            }
        }
    }
}