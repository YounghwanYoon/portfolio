package com.example.portfolio.feature_pagination3.data.mapper

import com.example.portfolio.feature_pagination3.data.local.entity.BeerEntity
import com.example.portfolio.feature_pagination3.data.remote.dto.BeerDto
import com.example.portfolio.feature_pagination3.domain.model.Beer


fun BeerDto.toBeerEntity(): BeerEntity {
    return BeerEntity(
        id = this.id,
        name = this.name,
        tagline = this.tagline,
        description = this.description,
        first_brewed = this.first_brewed,
        image_url = this.image_url
    )
}

fun BeerEntity.toBeer():Beer{
    return Beer(
        id = this.id,
        name = this.name,
        tagline = this.tagline,
        description = this.description,
        first_brewed = this.first_brewed,
        image_url = this.image_url
    )
}