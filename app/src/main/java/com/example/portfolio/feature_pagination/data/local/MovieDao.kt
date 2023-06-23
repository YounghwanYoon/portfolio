package com.example.portfolio.feature_pagination.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.portfolio.feature_pagination.data.local.entity.MovieEntity


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(movies: List<MovieEntity>)

    @Query("Select * From movie_entity Order By page")
    fun getMoviePagingSource():PagingSource<Int, MovieEntity>

    @Query("Delete From movie_entity")
    suspend fun clearAll()

}

