package com.example.portfolio.feature_shopping.domain.use_case

import com.example.portfolio.feature_shopping.domain.repository.ShoppingRepository
import javax.inject.Inject

class RemoveFromCart @Inject constructor(
    private val repository: ShoppingRepository
) {

}
