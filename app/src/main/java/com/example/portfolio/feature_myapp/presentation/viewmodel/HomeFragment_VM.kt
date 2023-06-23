package com.example.portfolio.feature_myapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class HomeFragment_VM(
    private val savedStateHandle: SavedStateHandle
    ): ViewModel() {

    companion object{
        private const val TAG = "HomeFragment_VM"
        const val SAVED_EDIT_TEXT = "savedEditText"
    }

    fun saveState(key:String, value:Any) {
        savedStateHandle.set(key,value)
    }

    private var savedText:String = ""
        set(value){
            field = value
            savedStateHandle.set(SAVED_EDIT_TEXT, value)
        }

    init{
        savedText = savedStateHandle.get<String>(SAVED_EDIT_TEXT) ?:""
    }




}