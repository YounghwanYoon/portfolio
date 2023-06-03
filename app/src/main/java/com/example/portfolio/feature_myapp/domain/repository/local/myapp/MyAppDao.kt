package com.example.portfolio.feature_myapp.domain.repository.local.myapp

import androidx.room.*
import com.example.demoapp.repository.local.myapp.MyAppLocal

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