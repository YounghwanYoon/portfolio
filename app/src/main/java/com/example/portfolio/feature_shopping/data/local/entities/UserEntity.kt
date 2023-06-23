package com.example.portfolio.feature_shopping.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName ="user")
data class UserEntity(
    @PrimaryKey
    val id:Long,
    val userId:Long,
    val pw:String,
    val email:String,
    val name:String,
)
