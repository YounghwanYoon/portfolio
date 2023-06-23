package com.example.portfolio.feature_pagination3.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.portfolio.feature_pagination3.data.local.entity.BeerEntity
import com.example.portfolio.feature_pagination3.data.mapper.toBeer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(
    //Purpose is to provide something easily observe and this case is for flow.
    // (there are other options: LiveData and RxJava
    pager: Pager<Int, BeerEntity>
):ViewModel() {

    val beerPagingFlow = pager
        .flow
        .map{ pagingData ->
            pagingData.map{it.toBeer()}
        }
        .cachedIn(viewModelScope)


}