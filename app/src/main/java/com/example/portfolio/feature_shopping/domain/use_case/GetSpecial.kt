package com.example.portfolio.feature_shopping.domain.use_case

import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.repository.ShoppingRepository
import com.example.portfolio.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpecialItem @Inject constructor(
    private val repository: ShoppingRepository
) {
    operator fun invoke(
        //consider adding order feature
    ): Flow<Resource<List<SpecialItem>>> {
        return repository.getSpecial()
    }
}