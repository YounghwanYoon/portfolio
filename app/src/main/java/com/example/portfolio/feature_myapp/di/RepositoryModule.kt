package com.example.portfolio.feature_myapp.di

import com.example.demoapp.repository.MainRepository
import com.example.portfolio.feature_myapp.domain.repository.local.myapp.MyAppDao
import com.example.demoapp.repository.webservices.myapp.LocalMapper
import com.example.demoapp.repository.webservices.myapp.NetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        /*apiServices: ApiServices,*/
        db_dao: MyAppDao,
        networkMapper: NetworkMapper,
        localMapper: LocalMapper
    ):MainRepository{
        return MainRepository(/*apiServices,*/ db_dao,networkMapper, localMapper)
    }


}