package com.example.portfolio.utils

interface EntityMapper<in I, out O> {

    fun mapFrom(data:I):O
    fun mapFromListOf(listData:List<I>):List<O>


}