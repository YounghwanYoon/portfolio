package com.example.portfolio.feature_shopping.data.mapper

import com.example.portfolio.feature_shopping.data.local.entities.SpecialItemEntity
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.utils.EntityMapper
import javax.inject.Inject

class SpecialItemMapperLocal @Inject constructor(): EntityMapper<SpecialItem,SpecialItemEntity> {
    override fun mapFrom(data: SpecialItem): SpecialItemEntity {
        return SpecialItemEntity(
            id = data.id,
            image = data.image,
            imageUrl = data.imageUrl,
            description = data.description,
            title = data.title,
            start_date = data.start_date,
            end_date = data.end_date
        )
    }
    override fun mapFromListOf(listData: List<SpecialItem>): List<SpecialItemEntity> {
        return listData.map{
            mapFrom(it)
        }
    }

    fun mapTo(data: SpecialItemEntity): SpecialItem {
        return SpecialItem(
            id = data.id,
            image = data.image,
            imageUrl = data.imageUrl,
            description = data.description,
            title = data.title,
            start_date = data.start_date,
            end_date = data.end_date
        )
    }
    fun mapToListOf(listData: List<SpecialItemEntity>): List<SpecialItem> {
        return listData.map{
            mapTo(it)
        }
    }
}



