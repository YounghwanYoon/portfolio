package com.example.portfolio.feature_pagination3.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.portfolio.feature_pagination3.data.local.BeerDataBase
import com.example.portfolio.feature_pagination3.data.local.entity.BeerEntity
import com.example.portfolio.feature_pagination3.data.remote.BeerApi
import com.example.portfolio.feature_pagination3.data.remote.BeerRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class BeerRemoteModule {

    @Provides
    @Singleton
    fun provideBeerApiService():BeerApi{
        return Retrofit.Builder()
            .baseUrl(BeerApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(BeerApi::class.java)
    }


    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideBeerPager(beerDB:BeerDataBase, beerApi:BeerApi): Pager<Int, BeerEntity>{
        return Pager(
            config = PagingConfig(
                pageSize = 20,
            ),
            remoteMediator = BeerRemoteMediator(beerDB, beerApi),
            pagingSourceFactory = { // which factory produce pagingSource
                beerDB.dao.pagingSource()
            }
        )
    }


}