package com.example.portfolio.feature_shopping.presentation.utils

sealed class SavedStateKeys(key:String){
    data class User(val key:String = "user"): SavedStateKeys(key)
    data class Cart(val key:String = "cart"): SavedStateKeys(key)
    data class Payment(val key:String = "payment"): SavedStateKeys(key)
    data class SellingItem(val key:String = "sellingItemsDTO"): SavedStateKeys(key)
    data class SpecialItem(val key:String = "specialItems"): SavedStateKeys(key)
    data class SelectedItem(val key:String = "selectedItem"): SavedStateKeys(key)
}