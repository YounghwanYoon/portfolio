package com.example.portfolio.feature_shopping.domain.use_case

import com.example.portfolio.feature_shopping.data.repository.ShoppingReposImpl
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import javax.inject.Inject

class AddToCart @Inject constructor(
    private val repository: ShoppingRepository
) {

    suspend operator fun invoke(sellingItem: SellingItem){
        repository.insertItem(ShoppingReposImpl.ShoppingDataType.SellingItem(sellingItem))
    }

}
