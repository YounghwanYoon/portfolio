package com.example.portfolio.feature_shopping.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.portfolio.feature_shopping.data.local.entities.PaginationKeysEntity


@Dao
interface PagingKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey:List<PaginationKeysEntity>)

    @Query("Select * From paginationkeysentity Where sellingItemId = :id")
    suspend fun getKeysByItemID(id:Int):PaginationKeysEntity

    @Query("Select * From paginationkeysentity ORDER BY created_at DESC LIMIT 1")
    suspend fun getLastKeys():PaginationKeysEntity


    @Query("Delete From paginationkeysentity")
    suspend fun clearRemoteKeys()

    @Query("Select created_at From paginationkeysentity Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?

}