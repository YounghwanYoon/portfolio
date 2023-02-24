package com.example.portfolio.feature_shopping.presentation.utils

import java.math.RoundingMode
import java.text.DecimalFormat

object Helper {

    fun formatHelper(value:Double):Double{
        val decimalFormatter = DecimalFormat("#.##")
        decimalFormatter.roundingMode = RoundingMode.HALF_UP

        return decimalFormatter.format(value).toDouble()
    }
}