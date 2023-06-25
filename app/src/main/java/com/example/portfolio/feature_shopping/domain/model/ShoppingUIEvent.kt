package com.example.portfolio.feature_shopping.domain.model

sealed class ShoppingUIEvent(val selected:Any){
    data class SelectItem(val selectItem: SellingItem):ShoppingUIEvent(selectItem)
    data class AddToCart(val selectItem: SellingItem):ShoppingUIEvent(selectItem)
    data class RequestedDetail(val selectedID:Int):ShoppingUIEvent(selectedID as Int)
    data class DeleteFromCart(val selectItem: SellingItem):ShoppingUIEvent(selectItem)
    data class IncreaseQuantity(val selectItem: SellingItem):ShoppingUIEvent(selectItem)
    data class DecreaseQuantity(val selectItem: SellingItem):ShoppingUIEvent(selectItem)
    object AppLaunched:ShoppingUIEvent(Unit)

}
