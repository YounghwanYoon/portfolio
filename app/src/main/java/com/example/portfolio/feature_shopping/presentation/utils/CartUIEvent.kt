package com.example.portfolio.feature_shopping.presentation.utils

import com.example.portfolio.feature_shopping.domain.model.SellingItem

sealed interface CartUIEvent{
    data class AddToCart(val selectedItem: SellingItem, val quantity:Int = 1):CartUIEvent
    data class RemoveFromCart(val selectedItem: SellingItem):CartUIEvent
    data class ReduceFromCart(val selectedItem: SellingItem, val quantity:Int = 1):CartUIEvent
    object RemoveAllFromCart:CartUIEvent
}
