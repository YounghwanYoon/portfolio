package com.example.portfolio.feature_shopping.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ShoppingListStateViewModel(

): ViewModel() {
    private val TAG = this.javaClass.name

    val counter:MutableState<Int> = mutableStateOf(0)

    fun addCounter (){
        counter.value++
    }

    fun substractCounter(){
        counter.value--
    }

    val shoppingList: MutableState<MutableList<ShoppingItem>> = mutableStateOf(mutableListOf())

    fun addToCart(newItem:ShoppingItem){
        var result  =shoppingList.value.add(newItem)
        Log.d(TAG, "addToCart: is called ")
    }
    fun removeFromCart(removeItem: ShoppingItem){
        shoppingList.value.remove(removeItem)
        Log.d(TAG, "removeFromCart: is called")
    }

}

data class ShoppingItem(
    val id:Int,
    val image:Int,
    val price:Int,
)
