package com.example.portfolio.feature_shopping.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class PaginationKeysEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?= null,
    val sellingItemId:Int,
    val prePage:Int?,
    val curPage:Int?,
    val nextPage:Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()

)
