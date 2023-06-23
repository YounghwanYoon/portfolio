package com.example.portfolio.feature_shopping.data.remote.dto


import com.google.gson.annotations.SerializedName

data class PixabayDTO(
    @SerializedName("hits")
    val items: List<items>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalHits")
    val totalHits: Int
)