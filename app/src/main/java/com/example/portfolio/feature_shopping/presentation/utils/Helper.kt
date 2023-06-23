package com.example.portfolio.feature_shopping.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.example.portfolio.feature_shopping.domain.model.Cart
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Locale
import kotlin.math.roundToInt

object Helper {

    @Composable
    fun isRoated():Boolean = LocalConfiguration.current.screenWidthDp > LocalConfiguration.current.screenHeightDp

    fun formatHelper(value:Double):Double{
        val decimalFormatter = DecimalFormat("#.##")
        decimalFormatter.roundingMode = RoundingMode.HALF_UP

        return decimalFormatter.format(value).toDouble()
    }
    fun  formatDoubleToString(value:Double):String{
/*        val decimalFormatter = DecimalFormat("#.##")
        decimalFormatter.roundingMode = RoundingMode.HALF_UP
        return decimalFormatter.format(value)*/

        return String.format(Locale.US, "%.2f", value)
    }

    fun formatToTwoDecimal(value:Double):Double{
        return (value * 100.0).roundToInt()/100.0

    }

    val Int.nonScaledSp
        @Composable
        get() = (this/ LocalDensity.current.fontScale).sp


    fun updateCart(updatedCart: Cart, key:SavedStateKeys){
        SavedStateHelper.updateSavedState(SavedStateKeys.Cart(), updatedCart)
        SavedStateHelper.updateSavedState(key, updatedCart)
        //SavedStateHandle()[(SAVEDSTATEKEYS.CART)] = _cartData
    }
}