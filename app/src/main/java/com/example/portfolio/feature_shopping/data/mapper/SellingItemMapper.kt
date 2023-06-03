package com.example.portfolio.feature_shopping.data.mapper

import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.utils.EntityMapper
import javax.inject.Inject

class SellingItemMapperLocal @Inject constructor(): EntityMapper<SellingItem,SellingItemEntity> {
    override fun mapFrom(data: SellingItem): SellingItemEntity {
        return SellingItemEntity(
            itemId = data.id,
            cartId = null,
            image = 0,
            imageUrl = data.imageUrl,
            title = data.title,
            description = data.description,
            price = data.price,
            quantity = data.supplyQty
        )
    }
    override fun mapFromListOf(listData: List<SellingItem>): List<SellingItemEntity> {
        return listData.map{
            mapFrom(it)
        }
    }

    fun mapTo(data: SellingItemEntity): SellingItem {
        return SellingItem(
            id= data.itemId,
            image = 0,
            imageUrl = data.imageUrl,
            title = data.title,
            description = data.description,
            price = data.price,
            supplyQty = data.quantity
        )
    }
    fun mapToListOf(listData: List<SellingItemEntity>): List<SellingItem> {
        return listData.map{
            mapTo(it)
        }
    }
}



