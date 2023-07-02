package com.example.portfolio.feature_shopping.data.remote.dto


import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.google.gson.annotations.SerializedName
import kotlin.random.Random

data class SellingItemDTO(
    @SerializedName("comments")
    val comments: Int,
    @SerializedName("downloads")
    val downloads: Int,
    @SerializedName("fullHDURL")
    val fullHDURL: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageHeight")
    val imageHeight: Int,
    @SerializedName("imageSize")
    val imageSize: Int,
    @SerializedName("imageURL")
    val imageURL: String,
    @SerializedName("imageWidth")
    val imageWidth: Int,
    @SerializedName("largeImageURL")
    val largeImageURL: String,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("pageURL")
    val pageURL: String,
    @SerializedName("previewHeight")
    val previewHeight: Int,
    @SerializedName("previewURL")
    val previewURL: String,
    @SerializedName("previewWidth")
    val previewWidth: Int,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("user")
    val user: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("userImageURL")
    val userImageURL: String,
    @SerializedName("views")
    val views: Int,
    @SerializedName("webformatHeight")
    val webformatHeight: Int,
    @SerializedName("webformatURL")
    val webformatURL: String,
    @SerializedName("webformatWidth")
    val webformatWidth: Int
){
    fun toEntity():SellingItemEntity{
        return SellingItemEntity(
            itemId = this.id,
            cartId = null,
            imageUrl = this.imageURL,
            title = this.user,
            description = this.tags,
            price = Random.nextDouble(4.99,8.99),
            quantity = Random.nextInt(10,100),
        )
    }
}