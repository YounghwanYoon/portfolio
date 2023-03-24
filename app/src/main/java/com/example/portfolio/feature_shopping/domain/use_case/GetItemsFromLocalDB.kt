package com.example.portfolio.feature_shopping.domain.use_case


import com.example.portfolio.feature_shopping.data.repository.ShoppingReposImpl
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
class GetItemsFromLocalDB @Inject constructor(
    private val repository:ShoppingRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<SellingItem>>>{
        return flow <Resource<List<SellingItem>>>{
            emit(Resource.Loading(null))

            val listOfItems:List<SellingItem> =
                repository.getDataFromRoom(ShoppingReposImpl.ShoppingDataType.SellingItems()) as List<SellingItem>

            if(listOfItems.isNotEmpty()){
                emit(Resource.Success(listOfItems))
            }else{
                emit(Resource.Error("No data found from room database"))
            }

        }.flowOn(Dispatchers.IO)
    }







}

