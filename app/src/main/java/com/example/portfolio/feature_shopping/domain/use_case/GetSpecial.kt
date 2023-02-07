package com.example.portfolio.feature_shopping.domain.use_case

import android.util.Log
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSpecialItem @Inject constructor(
    private val repository: ShoppingRepository
) {
    private val TAG = this.javaClass.name
    
    operator fun invoke(
        //consider adding order feature
    ): Flow<Resource<List<SpecialItem>>> {
        return flow{

            repository.getSpecial().collect{
                when(it){
                    is Resource.Error -> {
                        Log.d(TAG, "invoke: Error is called")
                        handleError(it)
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "invoke: Loading is called")
                        emit(it)
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "invoke: Success from GetSpecialItem UseCase" +
                                "Total amount of new data from server is ${
                                    it.data?.size
                                } " + it.data?.get(0)?.imageUrl

                        )
                        emit(it)
                    }
                }
            }
        }
    }

    private fun handleError(resource: Resource.Error<List<SpecialItem>>) {
        Log.d(TAG, "handleError: message from error ${resource.message}")
    }
}