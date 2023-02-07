package com.example.portfolio.feature_shopping.data.local

import androidx.room.*
import com.example.portfolio.feature_shopping.data.local.entities.SpecialItemEntity

@Dao
interface SpecialItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: SpecialItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items:List<SpecialItemEntity>)

    @Transaction
    @Query("SELECT * FROM special_items WHERE id = :itemId")
    suspend fun getSelectedItem(itemId:Int):SpecialItemEntity

    @Transaction
    @Query("Select * FROM special_items")
    suspend fun getSpecialItems():List<SpecialItemEntity>


    //Delete by entity itself
    @Delete
    suspend fun deleteSpecialItem(SpecialItemEntity: SpecialItemEntity)

    //Delete by id
    @Query("Delete FROM special_items WHERE id = :selectedId")
    suspend fun deleteSpecialItem(selectedId: Int)

}