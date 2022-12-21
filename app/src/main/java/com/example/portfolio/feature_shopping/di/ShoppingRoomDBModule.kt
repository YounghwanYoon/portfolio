package com.example.portfolio.feature_shopping.di

import androidx.compose.Context
import androidx.room.Room
import com.example.portfolio.feature_myapp.domain.repository.local.shopping.CartDataBase
import com.example.portfolio.feature_shopping.data.local.CartDao
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
        @ApplicationContext
        context: Context
    ): CartDataBase {
        return Room.databaseBuilder(
            context,
            CartDataBase::class.java,
            CartDataBase.DATABASE_NAME
        )
        .build()
    }

    @Singleton
    @Provides
    fun provideCartDao(shoppingDB: CartDataBase):CartDao{
        return shoppingDB.getShoppingDao()
    }
}