package com.example.portfolio.feature_shopping.di

import androidx.compose.Context
import androidx.room.Room
import com.example.portfolio.feature_shopping.data.local.CartDao
import com.example.portfolio.feature_shopping.data.local.SellingItemDao
import com.example.portfolio.feature_shopping.data.local.ShoppingDataBase
import com.example.portfolio.feature_shopping.data.local.SpecialItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShoppingRoomDBModule {

    @Singleton
    @Provides
    fun provideShoppingDB(
        context: Context
    ): ShoppingDataBase {
        return Room.databaseBuilder(
            context,
            ShoppingDataBase::class.java,
            ShoppingDataBase.CARTDB_NAME
        )
        .build()
    }

    @Singleton
    @Provides
    fun provideCartDao(
        cartDB: ShoppingDataBase,
        context: Context
    ):CartDao{
        return ShoppingDataBase.getInstance(context).cartDao()
    }
    @Singleton
    @Provides
    fun provideSpecialDao(
        cartDB: ShoppingDataBase,
        context: Context
    ):SpecialItemDao{
        return ShoppingDataBase.getInstance(context).specialItemDao()
    }


    @Singleton
    @Provides
    fun provideSellingItemDao(
        cartDB: ShoppingDataBase,
        context: Context
    ):SellingItemDao{
        return ShoppingDataBase.getInstance(context).sellingItemDao()
    }

    @Singleton
    @Provides
    fun getContext(
        @ApplicationContext
        context: Context
    ):Context = context
}