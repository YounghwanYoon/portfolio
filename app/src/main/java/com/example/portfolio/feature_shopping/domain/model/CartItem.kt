package com.example.portfolio.feature_shopping.domain.model

import androidx.compose.ui.graphics.painter.Painter

data class CartItem(
    val id:Int,
    var itemTitle:String = "I am the title",
    var image: Painter,
    var count:Int,
    var price: Double,
)