package com.example.portfolio.feature_shopping.domain.use_case

import androidx.lifecycle.SavedStateHandle
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.utils.ConstKeys
import com.example.portfolio.feature_shopping.presentation.utils.SavedStateHelper
import com.example.portfolio.feature_shopping.presentation.utils.SavedStateKeys
import javax.inject.Inject

class RemoveReduceFromCart @Inject constructor(
    private val _repository: ShoppingRepository,
    //val savedStateHandle: SavedStateHandle,
) {
   /* companion object {
        private const val TAG = "RemoveFromCart"
    }

    private var _cart
        get() = _cartData.value
        set(_cart){
            _cartData.value = _cart
            savedStateHandle.set(SAVEDSTATEKEYS.CART,_cart)
        }

    fun removeItem(selectedItem: SellingItem):Boolean{
        Log.d(TAG, "reduceItem: is called")
        _cart.apply{
            this.totalQuantity -= 1
            this.subTotal = 0.00
            this.items.remove(selectedItem)
            return true
        }
        //return false
    }
    fun reduceItem(selectedItem: SellingItem):Boolean{
        Log.d(TAG, "removeItem: isCalled")
        _cart.let{cart ->
            when(cart.items[selectedItem]){
                null ->{
                    //do nothing
                    throw Exception("Item should not be in cart. Therefore, this should not be called")
                }
                1-> {
                    removeItem(selectedItem)
                }
                else -> { //greater than 1
                    cart.items[selectedItem] = cart.items[selectedItem]!! - 1
                    cart.totalQuantity -= 1
                    cart.subTotal -= selectedItem.price
                }
            }
            return true
        }
        //return false
    }*/

    private var _cartData: Cart =  SavedStateHandle().get<Cart>(ConstKeys.CART) ?: Cart()

    fun removeAllItem(){
        _cartData = Cart()
        updateCart()
    }
    fun removeItem(selectedItem: SellingItem):Boolean{
        println( "reduceItem: is called")
        _cartData.apply{
            this.totalQuantity -= 1
            this.subTotal = 0.00
            this.items.remove(selectedItem)
            return true
        }
        //return false
        updateCart()
    }
    fun reduceItem(selectedItem: SellingItem):Boolean{
        println("removeItem: isCalled")
        _cartData.let{cart ->
            when(cart.items[selectedItem]){
                null ->{
                    //do nothing
                    throw Exception("Item should not be in cart. Therefore, this should not be called")
                }
                1-> {
                    removeItem(selectedItem)
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
        //return false
    }

    private fun updateCart(){
        SavedStateHelper.updateSavedState(SavedStateKeys.Cart(), _cartData)
        //SavedStateHandle()[(SAVEDSTATEKEYS.CART)] = _cartData
    }

}
