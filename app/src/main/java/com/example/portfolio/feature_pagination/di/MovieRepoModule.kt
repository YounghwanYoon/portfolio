package com.example.portfolio.feature_pagination.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.portfolio.BuildConfig
import com.example.portfolio.feature_pagination.data.local.MovieRoomDB
import com.example.portfolio.feature_pagination.data.local.entity.MovieEntity
import com.example.portfolio.feature_pagination.data.remote.mediator.MoviesRemoteMediator
import com.example.portfolio.feature_pagination.data.remote.service.MovieService
import com.example.portfolio.feature_pagination.data.repo_impl.MoviesRepositoryImpl
import com.example.portfolio.feature_pagination.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
class MovieRepoModule {

    @Provides
    @Singleton
    @Named("TBDM_API_KEY")
    fun provideAPIKey() = BuildConfig.TBDM_API_KEY

    @Provides
    @Singleton
    fun provideMovieRetrofit():Retrofit {
        return Retrofit.Builder()
            .baseUrl(MovieService.BASE_URL)
            //.client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideMovieService(
        retrofit: Retrofit,
    ):MovieService{
        return retrofit.create(MovieService::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class MovieRepoBinders{
        @Binds
        abstract fun  bindShoppingRepository(repository: MoviesRepositoryImpl): MoviesRepository
    }
/*
    @Provides
    @Singleton
    fun provideMovieMediator(
        @Named("TBDM_API_KEY")
        key:String,
        movieService:MovieService,
        movieRoomDB:MovieRoomDB
    ): MovieMediator {
        return MovieMediator(
            key = key,
            movieService = movieService,
            movieRoomDB = movieRoomDB
        )
    }

    @Provides
    @Singleton
    fun provideMoviePager(
        movieMediator: MovieMediator,
        movieDB:MovieRoomDB
    ): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = movieMediator,
            pagingSourceFactory = {
                movieDB.movieDao.getMoviePagingSourc()
            }
        )
    }*/

    @Provides
    @Singleton
    fun provideMovieMediator(
        movieService:MovieService,
        movieRoomDB:MovieRoomDB
    ): MoviesRemoteMediator {
        return MoviesRemoteMediator(
            moviesApiService = movieService,
            moviesDatabase = movieRoomDB
        )
    }

    @Provides
    @Singleton
    fun provideMoviePager(
        movieMediator: MoviesRemoteMediator,
        movieDB:MovieRoomDB
    ): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = movieMediator,
            pagingSourceFactory = {
                movieDB.movieDao.getMoviePagingSource()
            }
        )
    }
/*
    @Provides
    @Singleton
    fun provideMoviePager(
        movieMediator: MoviesRemoteMediator,
        movieDB:MovieRoomDB
    ): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = movieMediator,
            pagingSourceFactory = {
                movieDB.movieDao.getMoviePagingSourc()
            }
        )
    }*/

/*
    @Provides
    @Singleton
    fun provideMoviePagingData(
        movieMediator: MovieMediator,
        movieDB:MovieRoomDB
    ): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = PAGE_DISTANCE,
                initialLoadSize = PAGE_SIZE
            ),
            remoteMediator = movieMediator,
            pagingSourceFactory = {
                movieDB.movieDao.getMoviePagingSourc()
            }
        ).flow
    }*/
}

private val PAGE_SIZE = 20
private val PAGE_DISTANCE = 10