package com.example.portfolio.feature_shopping.di

import androidx.lifecycle.ViewModel
import com.example.portfolio.feature_shopping.presentation.cart.CartStateViewModel
import com.example.portfolio.feature_shopping.presentation.payment.PaymentViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton


/*@Module
@InstallIn(ViewModelComponent::class)
abstract class ShoppingViewModelBinder {

    @ViewModelScoped
    @Binds
    abstract fun bindCartViewModel(
        cartVM:CartStateViewModel
    ): ViewModel

    @Singleton
    @Binds
    abstract fun bindPaymentViewModel(
        paymentVM:PaymentViewModel
    ): ViewModel

}*/


