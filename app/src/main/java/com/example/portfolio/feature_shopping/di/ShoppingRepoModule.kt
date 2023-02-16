package com.example.portfolio.feature_shopping.di

import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import com.example.portfolio.feature_shopping.data.remote.PixabayAPI
import com.example.portfolio.feature_shopping.data.repository.ShoppingReposImpl
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.feature_shopping.domain.use_case.*
import com.example.portfolio.utils.SavedStateKeys
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ShoppingRepoModule{
    //private const val BASE_URL = "https://pixabay.com"
    private const val BASE_URL = "http://pixabay.com"

    @Singleton
    @Provides
    @Named("PixabayKey")
    fun providePixabayKEY():String = "15227824-a2215005f70965bf20cc7de51"
/*
    @Singleton
    @Provides
    fun provideRepository(
        @Named("ShoppingAPI")
        api:PixabayAPI
    ): ShoppingRepository{
        return ShoppingReposImpl(
            pixabayApi = api,
        )
    }*/



    @Module
    @InstallIn(SingletonComponent::class)
    abstract class ShoppingRepoBinders{
        @Binds
        abstract fun  bindShoppingRepository(repository: ShoppingReposImpl): ShoppingRepository
    }

    @Singleton
    @Provides
    @Named("ShoppingAPI")
    fun provideAPI(
        @Named("ShoppingRetrofit")
        retrofit: Retrofit
    ):PixabayAPI{
        return retrofit.create(PixabayAPI::class.java)
    }

    @Singleton
    @Provides
    @Named("ShoppingRetrofit")
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



}


@Module
@InstallIn(SingletonComponent::class)
object ShoppingUseCaseModule {

    @Singleton
    @Provides
    fun provideShoppingUseCase(
        repository: ShoppingRepository,
        //cart: Cart,
    ):ShoppingUseCases{
        return ShoppingUseCases(
            getSpecial = GetSpecialItem(
                repository
            ),
            getRegular = GetRegularItem(
                repository
            ),
            addToCart = AddToCart(
                _repository = repository,
                //_cartData = cart,
            ),
            removeCart = RemoveReduceFromCart(
                _repository = repository,
                //_cartData = cart,
            ),
            getCart = GetCart(
                //cart = cart
            )
        )
    }

   /* @Provides
    fun provideCart(
        savedStateHandle:SavedStateHandle
    ): Cart {
        return savedStateHandle.get(SavedStateKeys.CART) ?: Cart()
    }*/

}