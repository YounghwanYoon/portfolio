package com.example.portfolio.feature_shopping.domain.use_case

import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRegularItem @Inject constructor(
    private val repository: ShoppingRepository,
){
    operator fun invoke(): Flow<Resource<List<SellingItem>>> {
        return repository.fetchAndLoadSellingItems()
    }
}