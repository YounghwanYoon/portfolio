package com.example.portfolio.feature_shopping.domain.repository.webservices

import androidx.paging.PagingData
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.utils.ShoppingDataType
import com.example.portfolio.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    /**
     *  Call API Service to fetch Special data, store response to room database then emit to caller
     */
    fun fetchAndLoadSpecialItems(): Flow<Resource<List<SpecialItem>>>
    /**
     *  Call API Service to fetch Selling data, store response to room database then emit to caller
     */
    fun fetchAndLoadSellingItems(): Flow<Resource<List<SellingItem>>>
    suspend fun getDataFromAPI(dataType: ShoppingDataType):Any?



    /**
     * Find item by unique ID from Room DataBase
     * @param id Int
     * @return Flow<SellingItem>
     */
    suspend fun getItemById(id:Int): Flow<SellingItem>

    /**
     * In this project,there are two data type, Selling and Special and by providing which type
     * it will use to store data into room database
     * @param dataType ShoppingDataType
     */
    suspend fun insertItemToDataBase(dataType:ShoppingDataType)
    suspend fun deleteItemFromDataBase(dataType:ShoppingDataType)
    suspend fun getDataFromRoom(dataType: ShoppingDataType):List<Any>
    /**
     * This is limitless data calling,called pagination.
     * Call API Service to fetch Selling data, store response to room database then emit to caller as Pager
     * @return Flow<PagingData<SellingItem>>
     */
    fun getPagingData(): Flow<PagingData<SellingItem>>
}