package com.example.portfolio.feature_shopping.domain.use_case

import com.example.portfolio.feature_shopping.data.repository.ShoppingReposImpl
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import javax.inject.Inject

class GetDetailItem @Inject constructor(
    private val repo: ShoppingRepository
){
    suspend operator fun invoke(id:Int): Flow<SellingItem> {
        return repo.getItemById(id)
    }
}