package com.example.portfolio.feature_shopping.data.mapper

import com.example.portfolio.feature_shopping.data.local.entities.SpecialItemEntity
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.data.local.util.NetworkMapper
import com.example.portfolio.feature_shopping.data.remote.dto.SellingItemDTO
import com.example.portfolio.feature_shopping.domain.model.util.EntityMapper
import javax.inject.Inject

class SpecialItemMapper @Inject constructor():
    NetworkMapper<SellingItemDTO, SpecialItemEntity>,
    EntityMapper<SpecialItemEntity, SpecialItem>
{


    override fun mapFromDTO(data: SellingItemDTO): SpecialItemEntity {
        return SpecialItemEntity(
            id = data.id,
            image = 0,
            imageUrl = data.webformatURL,
            description = "aeque",
            title = "nonumy",
            start_date = "malesuada",
            end_date = "qui",
        )
    }


    override fun mapFromListDTO(listData: List<SellingItemDTO>): List<SpecialItemEntity> {
        return listData.map{
            mapFromDTO(it)
        }
    }

    override fun mapFromEntity(entity: SpecialItemEntity): SpecialItem {
        return SpecialItem(
            id = entity.id,
            image = entity.image,
            imageUrl = entity.imageUrl,
            description = entity.description,
            title = entity.title,
            start_date = entity.start_date,
            end_date = entity.end_date
        )
    }

    override fun mapFromEntities(entities: List<SpecialItemEntity>): List<SpecialItem> {
        return entities.map{
            mapFromEntity(it)
        }
    }

    override fun mapToEntity(model: SpecialItem): SpecialItemEntity {
        TODO("Not yet implemented")
    }

    override fun mapToEntities(models: List<SpecialItem>): List<SpecialItemEntity> {
        TODO("Not yet implemented")
    }
}



