package com.example.portfolio.feature_shopping.presentation.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.use_case.AddToCart
import com.example.portfolio.feature_shopping.domain.use_case.GetCart
import com.example.portfolio.feature_shopping.domain.use_case.RemoveReduceFromCart
import com.example.portfolio.feature_shopping.presentation.utils.CartUIEvent
import com.example.portfolio.utils.SavedStateKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class CartStateViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCart: GetCart,
    private val addToCart: AddToCart,
    private val removeReduceFromCart: RemoveReduceFromCart
): ViewModel() {
    private val TAG = this.javaClass.name
    var cartUIState by mutableStateOf<Cart>(savedStateHandle.get<Cart>(SavedStateKeys.CART) ?: Cart())
        private set
    var subTotal by mutableStateOf<Double>(cartUIState.subTotal)
        private set
    fun addItem(selectedItem: SellingItem){
        cartUIState.let{
            if(isContain(selectedItem,it)){
                it.items[selectedItem] = it.items[selectedItem]!! + 1
                it.totalQuantity += 1
                it.subTotal = formatHelper( subTotal + selectedItem.price)
                subTotal = it.subTotal
            }else{
                it.items[selectedItem] = 1
                it.totalQuantity += 1
                it.subTotal = formatHelper( subTotal + selectedItem.price)
                subTotal = it.subTotal
            }
            updateCart()
        }
    }
    fun reduceItem(selectedItem:SellingItem):Boolean{
        Log.d(TAG, "removeItem: isCalled")
        cartUIState.let{cart ->
            when(cart.items[selectedItem]){
                null ->{
                    //do nothing
                    throw Exception("Item should not be in cart. Therefore, this should not be called")
                }
                1-> {
                    removeItem(selectedItem)
                }
                else -> { //greater than 1
                    cart.let{
                        it.items[selectedItem] = it.items[selectedItem]!! - 1
                        it.totalQuantity -= 1
                        it.subTotal = formatHelper(it.subTotal- selectedItem.price)
                        subTotal = it.subTotal
                    }
                }
            }
            updateCart()
            return true
        }
        //return false
    }
    fun removeItem(selectedItem: SellingItem):Boolean{
        cartUIState.let{
            it.totalQuantity -= 1
            it.subTotal = formatHelper(it.subTotal - (selectedItem.price * it.items[selectedItem]!!))
            subTotal = it.subTotal
            it.items.remove(selectedItem)
            updateCart()
            return true
        }
        //return false
    }
    private fun updateCart() {
        savedStateHandle[SavedStateKeys.CART] = cartUIState
    }
    private fun formatHelper(value:Double):Double{
        val decimalFormatter = DecimalFormat("#.##")
        decimalFormatter.roundingMode = RoundingMode.HALF_UP

        return decimalFormatter.format(value).toDouble()
    }

    fun getCurItemPrice(selectItem:SellingItem):Double{
        //Quantity times price
        return cartUIState.items[selectItem]!!.times(selectItem.price)
    }
    private fun isContain(selectedItem:SellingItem, fromData:Cart):Boolean{
        return fromData.items.containsKey(selectedItem)
    }

    fun setCartUIEvent(event : CartUIEvent){
        when(event){
            is CartUIEvent.AddToCart -> {
                addItem(selectedItem = event.selectedItem)
            }
            is CartUIEvent.ReduceFromCart -> {
                reduceItem(selectedItem = event.selectedItem)
            }
            is CartUIEvent.RemoveFromCart -> {
                removeItem(selectedItem = event.selectedItem)
            }
        }
    }
}


