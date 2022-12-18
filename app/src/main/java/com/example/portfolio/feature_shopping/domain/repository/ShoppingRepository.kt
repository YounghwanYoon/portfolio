package com.example.portfolio.feature_shopping.domain.repository

import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    fun getSpecial(): Flow<Resource<List<SpecialItem>>>
    fun getSellingItem(): Flow<Resource<List<SellingItem>>>

    suspend fun getItemById(id:Int): SellingItem?
    suspend fun insertItem(sellingItem:SellingItem)
    suspend fun deleteItem(sellingItem:SellingItem)

}