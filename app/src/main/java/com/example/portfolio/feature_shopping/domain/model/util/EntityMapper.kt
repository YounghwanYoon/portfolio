package com.example.portfolio.feature_shopping.domain.model.util

interface EntityMapper<in I, out O> {
    fun mapFromEntity (entity:I):O
    fun mapFromEntities(entities:List<I>):List<O>

    fun mapToEntity(model: @UnsafeVariance O): @UnsafeVariance I
    fun mapToEntities(models:List<@UnsafeVariance O>):List<@UnsafeVariance I>
}