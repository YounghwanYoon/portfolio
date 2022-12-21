package com.example.portfolio.feature_shopping.domain.model

data class SellingItem(
    var id:Int,
    var image: Int = 0,
    var imageUrl:String = "",
    var title:String,
    var description:String = "null ",
    var price:Double,
    var quantity:Int = 99
){

}

