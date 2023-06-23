package com.example.portfolio.feature_myapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.feature_myapp.domain.repository.MainRepository
import com.example.demoapp.repository.local.myapp.MyAppLocal
import com.example.portfolio.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//learn channel kotlin

@HiltViewModel
class MyAppViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository
): ViewModel() {

    private val _dataState:MutableStateFlow<DataState<List<MyAppLocal>>> = MutableStateFlow(
        DataState.Loading())
    val dataUIState: StateFlow<DataState<List<MyAppLocal>>> = _dataState


    fun setStateEvent(mainStateEvent: MainStateEvent){
        /*viewModelScope.launch{
            when(mainStateEvent){
                is MainStateEvent.GetMyAppEvents -> {
                    Log.d(TAG, "setStateEvent: MainStateEvent.GetMyAppEvents ")
                   mainRepository.getMyApp()
                       .onEach { dataState ->
                           _dataState.value = dataState
                       }.launchIn(this)
                }
                is MainStateEvent.NoneEvents -> {

                }
            }

        }*/

    }

    private val TAG:String = this.javaClass.name


}

sealed class MainStateEvent{
    object GetMyAppEvents: MainStateEvent()
    object NoneEvents: MainStateEvent()

}