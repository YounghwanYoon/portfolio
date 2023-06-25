package com.example.portfolio.feature_shopping.domain.repository.webservices

import androidx.paging.PagingData
import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.example.portfolio.feature_shopping.data.repository.ShoppingReposImpl
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    fun fetchAndLoadSpecialItems(): Flow<Resource<List<SpecialItem>>>
    fun fetchAndLoadSellingItems(): Flow<Resource<List<SellingItem>>>
    suspend fun getDataFromAPI(data: ShoppingReposImpl.ShoppingDataType):Any?
    //suspend fun getDataFromRoom(data: ShoppingReposImpl.ShoppingDataType):List<Any>
    suspend fun getDataFromRoom(data: ShoppingReposImpl.ShoppingDataType):List<Any>
    fun getPagingDate(): Flow<PagingData<SellingItem>>
    suspend fun getItemById(id:Int): Flow<SellingItem>
//    suspend fun getItemById(id:Int): SellingItem?*/
    suspend fun insertItem(dataType:ShoppingReposImpl.ShoppingDataType)
    suspend fun deleteItem(dataType:ShoppingReposImpl.ShoppingDataType)

}