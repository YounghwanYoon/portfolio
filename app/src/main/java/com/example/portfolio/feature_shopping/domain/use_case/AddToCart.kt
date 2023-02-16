package com.example.portfolio.feature_shopping.domain.use_case

import androidx.lifecycle.SavedStateHandle
import com.example.portfolio.feature_shopping.data.repository.ShoppingReposImpl
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.utils.SavedStateKeys
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class AddToCart @Inject constructor(
    private val _repository: ShoppingRepository,
    //private val _cartData: Cart,
    //private val savedStateHandle: SavedStateHandle
) {
/*    private var _cart
        get() = _cartData.value
        set(_cart){
            _cartData.value = _cart
            //savedStateHandle.set(SavedStateKeys.CART,_cart)
        }

    operator fun invoke(selectedItem: SellingItem){
        addItem(selectedItem = selectedItem)
    }
    fun addItem(selectedItem: SellingItem):Boolean{
        _cart.let{
            if(isContain(selectedItem,it)){
                it.items[selectedItem] = it.items[selectedItem]!! + 1
                it.totalQuantity += 1
                it.subTotal += selectedItem.price
            }else{
                it.items[selectedItem] = 1
                it.totalQuantity += 1
                it.subTotal += selectedItem.price
            }
            //_cart.copy(it)
            return true
        }
        //return false
    }
    private fun isContain(selectedItem:SellingItem, fromData:Cart):Boolean{
        return fromData.items.containsKey(selectedItem)
    }*/

}
