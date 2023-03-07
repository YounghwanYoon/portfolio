package com.example.portfolio.feature_shopping.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import java.math.RoundingMode
import java.text.DecimalFormat

object Helper {

    fun formatHelper(value:Double):Double{
        val decimalFormatter = DecimalFormat("#.##")
        decimalFormatter.roundingMode = RoundingMode.HALF_UP

        return decimalFormatter.format(value).toDouble()
    }

    val Int.nonScaledSp
        @Composable
        get() = (this/ LocalDensity.current.fontScale).sp
}