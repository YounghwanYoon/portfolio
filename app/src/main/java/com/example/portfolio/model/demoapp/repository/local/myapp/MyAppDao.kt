package com.example.demoapp.repository.local.myapp

import androidx.room.*

@Dao
interface MyAppDao {

    @Query("SELECT * FROM myapplocal")
    suspend fun getAll():List<MyAppLocal>

    // Long return at which row, the data was inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(myApp: MyAppLocal): Long


    @Insert
    fun insertAll(myApps:List<MyAppLocal>)

    @Delete
    fun delete(myApp: MyAppLocal)

}