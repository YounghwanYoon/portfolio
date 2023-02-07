package com.example.portfolio.feature_shopping.domain.use_case

import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import javax.inject.Inject

class RemoveFromCart @Inject constructor(
    private val repository: ShoppingRepository
) {

}
