package com.example.portfolio.feature_pagination.presentation.movie_list.viewmodel

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.*
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.portfolio.feature_pagination.data.local.MovieRoomDB
import com.example.portfolio.feature_pagination.data.local.entity.MovieEntity
import com.example.portfolio.feature_pagination.data.local.entity.toMovie
import com.example.portfolio.feature_pagination.data.remote.mediator.MoviesRemoteMediator
import com.example.portfolio.feature_pagination.data.remote.service.MovieService
import com.example.portfolio.feature_pagination.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieApiService:MovieService,
    private val movieDB:MovieRoomDB,
    pager: Pager<Int, MovieEntity>,
):ViewModel() {


    @OptIn(ExperimentalPagingApi::class)
    fun getPager():Flow<PagingData<MovieEntity>> =
        Pager<Int, MovieEntity>(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = 20,
            ),
            pagingSourceFactory = {
                movieDB.movieDao.getMoviePagingSource()
            },
            remoteMediator =
                MoviesRemoteMediator(
                    movieApiService,
                    movieDB
                )
        ).flow


    val moviePagingFlow = pager
        .flow
        .map{pagingData ->
            pagingData.map{
                it.toMovie()
            }
        }
        .cachedIn(viewModelScope)



}