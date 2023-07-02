package com.example.portfolio.feature_shopping.domain.use_case

import android.util.Log
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class GetSpecialItem @Inject constructor(
    private val repository: ShoppingRepository
) {
    private val TAG = this.javaClass.name
    
    operator fun invoke(
        //consider adding order feature
    ): Flow<Resource<List<SpecialItem>>> {
        return flow{

            repository.fetchAndLoadSpecialItems().collect{
                when(it){
                    is Resource.Error -> {
                        Timber.tag(TAG).d("invoke: Error is called")
                        handleError(it)
                    }
                    is Resource.Loading -> {
                        Timber.tag(TAG).d("invoke: Loading is called")
                        emit(it)
                    }
                    is Resource.Success -> {
                        Timber.tag(TAG).d("fetching and loading was success")
                        emit(it)
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun handleError(resource: Resource.Error<List<SpecialItem>>) {
        Timber.tag(TAG).d("handleError: message from error %s", resource.message)
    }
}