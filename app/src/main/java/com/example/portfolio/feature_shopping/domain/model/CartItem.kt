package com.example.portfolio.feature_shopping.domain.model

data class CartItem(
    val sellingItem:SellingItem,
    val quantity:Int,
) {
}