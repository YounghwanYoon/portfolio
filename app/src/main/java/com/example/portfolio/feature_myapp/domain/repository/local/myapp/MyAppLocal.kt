package com.example.demoapp.repository.local.myapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyAppLocal(
    @PrimaryKey
    val id: Int,
    val title:String,
    val description: String,
    val picture:String,
    var picture_color:Int?,
    @ColumnInfo(name = "reference_url")
    val referenceURL:String

) {

}