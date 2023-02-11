package com.example.portfolio.feature_shopping.presentation.cart

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.utils.SavedStateKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartStateViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val TAG = this.javaClass.name

    private val _cartData = MutableStateFlow<Cart>(savedStateHandle.get(SavedStateKeys.CART) ?: Cart())
//https://stackoverflow.com/questions/70433526/how-can-we-save-and-restore-state-for-android-stateflow
    private var _cart
        get() = _cartData.value
        set(_cart){
            _cartData.value = _cart
            savedStateHandle.set(SavedStateKeys.CART,_cart)
        }

    val cart = _cartData.asStateFlow()//savedStateHandle.getStateFlow<Cart?>(SavedStateKeys.CART, Cart())

    fun addItem(newItem: SellingItem):Boolean{
        println("added item Quantity - ${cart.value?.totalQuantity}")
        _cart.let{
            if(isContain(newItem,it)){
                it.items[newItem] = it.items[newItem]!! + 1
                it.totalQuantity += 1
                it.subTotal += newItem.price
            }else{
                it.items[newItem] = 1
                it.totalQuantity += 1
                it.subTotal += newItem.price
            }
            updateCart()
            return true
        }
        //return false
    }

    fun removeItem(selectedItem:SellingItem):Boolean{
        _cart.let{cart ->
            when(cart.items[selectedItem]){
                null ->{
                    //do nothing
                    throw Exception("Item should not be in cart. Therefore, this should not be called")
                }
                1-> {
                    cart.items.remove(selectedItem)
                    cart.totalQuantity -= 1
                    cart.subTotal -= selectedItem.price
                }
                else -> { //greater than 1
                    cart.items[selectedItem] = cart.items[selectedItem]!! - 1
                    cart.totalQuantity -= 1
                    cart.subTotal -= selectedItem.price
                }
            }
            updateCart()
            return true
        }
        return false
    }
    private fun isContain(selectedItem:SellingItem, fromData:Cart):Boolean{
        return fromData.items.containsKey(selectedItem)
    }
    private fun updateCart(){

        _cart = _cart
/*        viewModelScope.launch{
            savedStateHandle.set(SavedStateKeys.CART, _cart.value)
        }*/
    }
    fun getSubTotal():Double = cart.value?.subTotal ?: 0.0
    fun getTotalQuantity():Int = cart.value?.totalQuantity ?: 0

}