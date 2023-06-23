package com.example.portfolio.feature_shopping.domain.use_case

import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.feature_shopping.presentation.utils.Helper.formatDoubleToString
import com.example.portfolio.feature_shopping.presentation.utils.Helper.updateCart
import com.example.portfolio.feature_shopping.presentation.utils.SavedStateKeys
import javax.inject.Inject

class AddToCart @Inject constructor(
    private val _repository: ShoppingRepository,
) {

    operator fun invoke(item: SellingItem, quantity:Int = 1, oldCart:Cart): Cart {
        println("addCart (): Current Item: $item, \n adding quantity: $quantity")

        val updatedCart:Cart = oldCart.copy(
            items = oldCart.items.apply{
                this.put(item, oldCart.items.get(item)?.plus(quantity) ?: quantity/**/)
            },

            /*mapValues {
                    (item.quantity + quantity)
            }.toMutableMap(),*/
            totalQuantity = oldCart.totalQuantity?.plus(quantity) ?: quantity,
            subTotal = formatDoubleToString( oldCart.subTotal.toDouble() + item.price*quantity)
        )

        println("After adding item cart: ${updatedCart}\n" +
                "item: ${updatedCart.items}\n" +
                "total Quantity: ${updatedCart.totalQuantity}" +
                "selectedItem: ${updatedCart.items[item]}"
        )

        updateCart(updatedCart, SavedStateKeys.Cart())
        return updatedCart

    }
    fun addToCart(item: SellingItem, quantity:Int = 1, oldCart:Cart): Cart {

        val updatedCart:Cart

        if(isContain(item, oldCart)) {
            updatedCart = oldCart.copy(
                items = oldCart.items.apply{
                    this.put(item, oldCart.items.get(item)?.plus(quantity) ?: quantity/**/)
                },

                /*mapValues {
                        (item.quantity + quantity)
                }.toMutableMap(),*/
                totalQuantity = oldCart.totalQuantity?.plus(quantity) ?: quantity,
                subTotal = formatDoubleToString( oldCart.subTotal.toDouble() + item.price*quantity)
            )/*
            map.put(key, map.get(key) + 1);*/
        }else{
            updatedCart =  oldCart.copy(
                items = oldCart.items.mapValues {
                    quantity
                }.toMutableMap(),
                totalQuantity = quantity,
                subTotal = formatDoubleToString( oldCart.subTotal.toDouble() + item.price*quantity)
            )
        }
        updateCart(updatedCart, SavedStateKeys.Cart())
        return updatedCart

    }

    private fun isContain(selectedItem:SellingItem, fromData:Cart):Boolean{
        return fromData.items.contains(selectedItem)
    }

/*    private var _cart
        get() = _cartData.value
        set(_cart){
            _cartData.value = _cart
            //savedStateHandle.set(SAVEDSTATEKEYS.CART,_cart)
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
