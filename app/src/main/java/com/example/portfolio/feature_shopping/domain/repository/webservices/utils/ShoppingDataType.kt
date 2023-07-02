package com.example.portfolio.feature_shopping.domain.repository.webservices.utils


sealed interface ShoppingDataType {
    class SpecialItem(val data: Any) : ShoppingDataType
    class SpecialItems(val listData: List<Any>? = null) : ShoppingDataType
    class SellingItem(val data: Any) : ShoppingDataType
    class SellingItems(val listData: List<Any>? = null) : ShoppingDataType
}
