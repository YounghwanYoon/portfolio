package com.example.portfolio.feature_pagination3.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.portfolio.feature_pagination3.data.local.entity.BeerEntity


@Database(
    entities = [BeerEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class BeerDataBase:RoomDatabase() {

    abstract val dao:BeerDao

}