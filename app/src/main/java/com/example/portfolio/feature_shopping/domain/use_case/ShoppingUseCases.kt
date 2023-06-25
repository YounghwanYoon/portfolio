package com.example.portfolio.feature_shopping.domain.use_case

data class ShoppingUseCases(
    val getSpecial:GetSpecialItem,
    val getRegular:GetRegularItem,
    val getDetail:GetDetailItem,
    val addToCart:AddToCart,
    val removeCart:RemoveReduceFromCart,
    val getCart:GetCart,
)
