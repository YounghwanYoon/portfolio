package com.example.portfolio.feature_shopping.domain.model

import android.os.Parcelable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var first_name:String = "Ray",
    var last_name:String = "Yoon",
    var email:String ="ray.yoon87@gmail.com",
    var address_shipping:String = "123456 El Camino Real",
    var address_mailing:String = "123456 El Camino Real",
    var city:String = "Arlington",
    var state:String = "TX",
    var zip:Int = 12345,
    var paymentInfo:List<PaymentInfo> = mutableStateListOf<PaymentInfo>(PaymentInfo()) ,
    var isLoading:Boolean = false
):Parcelable