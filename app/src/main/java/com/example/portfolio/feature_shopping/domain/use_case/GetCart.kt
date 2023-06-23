package com.example.portfolio.feature_shopping.domain.use_case

import androidx.lifecycle.SavedStateHandle
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.utils.ConstKeys
import javax.inject.Inject

class GetCart @Inject constructor(
) {
    operator fun invoke():Cart = SavedStateHandle().get<Cart>(ConstKeys.CART) ?: Cart()
}