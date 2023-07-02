package com.example.portfolio.feature_shopping.data.local.util

interface NetworkMapper<in I, out O> {
    fun mapFromDTO(data:I):O
    fun mapFromListDTO(listData:List<I>):List<O>
}