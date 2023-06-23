package com.example.portfolio.feature_pagination3.domain.model

data class Beer(
    val id:Int,
    val name:String,
    val tagline:String,
    val description:String,
    val first_brewed:String,
    val image_url:String?
)
