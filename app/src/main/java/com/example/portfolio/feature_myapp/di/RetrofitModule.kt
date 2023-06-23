package com.example.portfolio.feature_myapp.di

import com.example.demoapp.model.MyApp
import com.example.demoapp.repository.webservices.fakedata.FakeServer
import com.example.demoapp.repository.webservices.myapp.ApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@Named("GithubModule")
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    val BASE_URL = "https://api.github.com/"

    @Singleton
    @Named("GithubModule_retrofit")
    @Provides
    fun provideRetrofitBuilder(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiServices(@Named("GithubModule_retrofit") retrofit:Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideFakeData():List<MyApp>{
        return FakeServer.provideData()
    }


}