package com.example.demoapp.repository.local.myapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MyAppLocal::class], version = 1)
abstract class MyAppDataBase:RoomDatabase() {
    companion object{
        val DATABASE_NAME = "myapp_db"
    }

    abstract fun myAppDao(): MyAppDao
}