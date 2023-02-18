package com.example.portfolio.feature_shopping.presentation.utils

import com.example.portfolio.feature_shopping.domain.model.SellingItem

sealed class CartUIEvent(selectedItem: SellingItem){
    data class AddToCart(val selectedItem: SellingItem):CartUIEvent(selectedItem)
    data class RemoveFromCart(val selectedItem: SellingItem):CartUIEvent(selectedItem)
    data class ReduceFromCart(val selectedItem: SellingItem):CartUIEvent(selectedItem)
}
