package com.example.portfolio.feature_shopping.presentation.main.utils

sealed class ItemUiState<T>{
    data class Success<T>(val items: List<T>): ItemUiState<T>()
    data class Loading<T>(val items:List<T> = emptyList<T>()): ItemUiState<T>()
    data class Error (val exception:Throwable): ItemUiState<Nothing>()
}