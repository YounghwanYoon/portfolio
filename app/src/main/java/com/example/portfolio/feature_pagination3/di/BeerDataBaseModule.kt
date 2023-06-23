package com.example.portfolio.feature_pagination3.di

import android.content.Context
import androidx.room.Room
import com.example.portfolio.feature_pagination3.data.local.BeerDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class BeerDataBaseModule {
    @Provides
    @Singleton
    fun provideBeerDataBase(
        @ApplicationContext
        context: Context,
    ): BeerDataBase {
        return Room.databaseBuilder(
            context = context,
            klass = BeerDataBase::class.java,
            name = "beer.db"
        ).build()
    }

}