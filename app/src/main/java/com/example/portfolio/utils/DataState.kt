package com.example.demoapp.utils

import com.example.demoapp.repository.local.myapp.MyAppLocal


sealed class DataState {
    data class Error(var exception:Exception):DataState()
    data class Success(var myApps:List<MyAppLocal>):DataState()
    object Loading : DataState()
}

