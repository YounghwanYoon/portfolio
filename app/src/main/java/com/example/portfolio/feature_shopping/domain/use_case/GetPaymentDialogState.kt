package com.example.portfolio.feature_shopping.domain.use_case

import androidx.lifecycle.SavedStateHandle
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.utils.ConstKeys

class GetPaymentState(
    val savedStateHandle: SavedStateHandle = SavedStateHandle()
) {

    operator fun invoke(): Cart {
        return savedStateHandle.get(ConstKeys.CART) ?: Cart()
    }
}