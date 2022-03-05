package com.example.portfolio.di

import android.content.Context
import androidx.room.Room
import com.example.demoapp.repository.local.myapp.MyAppDao
import com.example.demoapp.repository.local.myapp.MyAppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideMyAppDataBase(@ApplicationContext context: Context,): MyAppDataBase {
        return Room.databaseBuilder(
            context,
            MyAppDataBase::class.java,
            MyAppDataBase.DATABASE_NAME

        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMyAppDao(myAppDB: MyAppDataBase): MyAppDao {
        return myAppDB.myAppDao()
    }


}