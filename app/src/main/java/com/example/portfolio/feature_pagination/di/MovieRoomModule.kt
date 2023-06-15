package com.example.portfolio.feature_pagination.di

import android.content.Context
import androidx.room.Room
import com.example.portfolio.feature_pagination.data.local.MovieDao
import com.example.portfolio.feature_pagination.data.local.MovieRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
class MovieRoomModule {

    @Provides
    fun provideDataBase(
        context: Context,
    ):MovieRoomDB{
        return Room.databaseBuilder(
            context = context,
            klass = MovieRoomDB::class.java,
            name = MovieRoomDB.MOVIE_DB_NAME,
        ).build()
    }

    @Provides
    fun provideMovieDao(
        movieDB:MovieRoomDB,
        @ApplicationContext
        context:Context
    ):MovieDao{
        return movieDB.movieDao
    }
}