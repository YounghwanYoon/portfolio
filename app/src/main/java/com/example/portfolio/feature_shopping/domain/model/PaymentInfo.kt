package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentInfo(
    var cardInfo:String = "1234567890",
    var cvv:String ="123",
    var expireDate:String ="01",
    var expireYear:String = "99",
    var default:Boolean = false
): Parcelable{
    @IgnoredOnParcel
    var lastFourDigit:String= lastCardDigits()

    /**
     * Returns last four digit of card information in string
     * @return String
     */
    private fun lastCardDigits():String = cardInfo.substring(cardInfo.length-1 -4, cardInfo.lastIndex)
}
