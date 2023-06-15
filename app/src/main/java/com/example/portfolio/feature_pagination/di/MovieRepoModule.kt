package com.example.portfolio.feature_pagination.di

import com.example.portfolio.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MovieRepoModule {

    @Provides
    fun provideAPIKey() = BuildConfig.TBDM_API_KEY



}