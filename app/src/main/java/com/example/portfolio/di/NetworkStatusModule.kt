package com.example.portfolio.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.portfolio.utils.NetworkStateTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkStatusModule {

    @Singleton
    @Provides
    @Named("NetworkDispatcher")
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun provideConnectivityManager(@ApplicationContext context:Context):ConnectivityManager{
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }



    @Singleton
    @Provides
    fun getNetworkStatusTracker():NetworkStateTracker{
        return NetworkStateTracker
    }

}

