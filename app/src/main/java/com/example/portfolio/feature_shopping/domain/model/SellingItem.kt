package com.example.portfolio.feature_shopping.domain.model

import androidx.compose.ui.graphics.painter.Painter
data class SellingItem(
    var id:Int,
    var image: Painter,
    var description:String,
    var type:DisplayType = DisplayType.Regular
){
    enum class DisplayType{
        SPECIAL, Regular
    }

}

