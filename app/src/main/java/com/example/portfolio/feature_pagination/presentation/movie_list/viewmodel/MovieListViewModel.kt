package com.example.portfolio.feature_pagination.presentation.movie_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.portfolio.feature_pagination.domain.model.util.MovieMapper
import com.example.portfolio.feature_pagination.domain.repository.MoviesRepository
import com.example.portfolio.feature_pagination.presentation.ui.MovieUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class MovieListViewModel(
    private val moviesRepository: MoviesRepository,
    private val mapper: MovieMapper
):ViewModel() {

    private val lazyPagingItems = MutableStateFlow<PagingData<MovieUi>>(getMovies().collect)


    fun getMovies(): Flow<PagingData<MovieUi>>{
        return moviesRepository.getMovies()
            .map{ pagingData ->
                pagingData.map{
                    mapper.mapDomainMovieToUi(domainMovie = it)
                }
            }
            .cachedIn(viewModelScope)
    }

}