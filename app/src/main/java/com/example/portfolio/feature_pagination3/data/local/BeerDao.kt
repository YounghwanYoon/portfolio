package com.example.portfolio.feature_pagination3.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.portfolio.feature_pagination3.data.local.entity.BeerEntity

@Dao
interface BeerDao {

    @Upsert
    suspend fun upsertAll(beers:List<BeerEntity>)

    /**
     * Int = key = page #
     */
    @Query("SELECT * FROM beerentity")
    fun pagingSource(): PagingSource<Int, BeerEntity>

    @Query("DELETE FROM beerentity")
    suspend fun clearAll()


}