package com.example.portfolio.feature_shopping.data.mapper

import com.example.portfolio.feature_shopping.data.local.entities.SellingItemEntity
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.data.local.util.NetworkMapper
import com.example.portfolio.feature_shopping.data.remote.dto.SellingItemDTO
import com.example.portfolio.feature_shopping.domain.model.util.EntityMapper
import javax.inject.Inject
import kotlin.random.Random

class SellingItemMapper @Inject constructor():
    NetworkMapper<SellingItemDTO, SellingItemEntity>,
    EntityMapper<SellingItemEntity, SellingItem>
{
    override fun mapFromDTO(data: SellingItemDTO): SellingItemEntity {
        return SellingItemEntity(
            itemId = data.id,
            cartId = null,
            imageUrl = data.imageURL,
            title = data.user,
            description = data.tags,
            price = Random.nextDouble(4.99,8.99),
            quantity = Random.nextInt(10,100),
        )
    }
    override fun mapFromListDTO(listData: List<SellingItemDTO>): List<SellingItemEntity> {
        return listData.map{
            mapFromDTO(it)
        }
    }

    override fun mapFromEntity(data: SellingItemEntity): SellingItem {
        return SellingItem(
            id= data.itemId,
            image = 0,
            imageUrl = data.imageUrl!!,
            title = data.title!!,
            description = data.description,
            price = data.price,
            supplyQty = data.quantity
        )
    }
    override fun mapFromEntities(listData: List<SellingItemEntity>): List<SellingItem> {
        return listData.map{
            mapFromEntity(it)
        }
    }

    override fun mapToEntity(model: SellingItem): SellingItemEntity {
        return SellingItemEntity(
            itemId = model.id,
            cartId = null,
            image = model.image,
            imageUrl = model.imageUrl,
            title = model.title,
            description = model.description,
            price = model.price,
            quantity = model.supplyQty,
        )
    }

    override fun mapToEntities(models: List<SellingItem>): List<SellingItemEntity> {
        return models.map{
            mapToEntity(it)
        }
    }
}



