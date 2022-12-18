package com.example.portfolio.feature_shopping.domain.model

sealed class ShoppingUIEvent(){
    data class SelectItem(val selectItem: SellingItem):ShoppingUIEvent()
    data class AddToCart(val selectItem: SellingItem):ShoppingUIEvent()
    data class DeleteFromCart(val selectItem: SellingItem):ShoppingUIEvent()
    data class IncreaseQuantity(val selectItem: SellingItem):ShoppingUIEvent()
    data class DecreaseQuantity(val selectItem: SellingItem):ShoppingUIEvent()
    object AppLaunched:ShoppingUIEvent()

}
