package com.example.portfolio.feature_shopping.presentation.utils

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object ShoppingColors {
    val PearlWhite = Color(0xFFEEF8EF)
    val DarkGrey = Color(0xE2403D3D)
    val GreenWhite = Color(0xFFE3FBC7)
    val LightGrey = Color(0xFF858484)

    val Brown_50 = Color (0xFFFFF6E5)
    val Brown_300 = Color (0xFFC7880E)
    val Brown_700 = Color (0XFF46271C)
    val Black = Color.Black

    //Day Theme
    val LightColors = lightColors(
        primary= Brown_50,
        primaryVariant = Brown_700,
        onPrimary = Black,
        background = PearlWhite
    )


    //Dark Theme
    private val DarkColors = darkColors(

    )
}