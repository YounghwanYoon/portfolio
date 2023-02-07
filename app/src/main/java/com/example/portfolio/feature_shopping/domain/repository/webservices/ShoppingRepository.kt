package com.example.portfolio.feature_shopping.domain.repository.webservices

import com.example.portfolio.feature_shopping.data.repository.ShoppingReposImpl
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    fun getSpecial(): Flow<Resource<List<SpecialItem>>>
    fun getSellingItem(): Flow<Resource<List<SellingItem>>>

    suspend fun getDataFromAPI(data: ShoppingReposImpl.ShoppingDataType):Any?

    suspend fun getDataFromRoom(data: ShoppingReposImpl.ShoppingDataType):List<Any>
    suspend fun getItemById(id:Int, dataType:ShoppingReposImpl.ShoppingDataType): SellingItem?
    suspend fun insertItem(dataType:ShoppingReposImpl.ShoppingDataType)
    suspend fun deleteItem(dataType:ShoppingReposImpl.ShoppingDataType)

}