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

        var updatedCart:Cart
        oldCart.apply{
            updatedCart = when{
                isContain(item,oldCart) -> {
                    println("add Invoke called and item is already contained ")
                    oldCart.copy(
                        items = this.items.apply{
                            this[item] = (this[item]?.plus(quantity)) ?: quantity
                        },
                        totalQuantity = this.totalQuantity + quantity,
                        subTotal = formatDoubleToString( this.subTotal.toDouble() + item.price*quantity)
                    )
                }
                !isContain(item,oldCart) -> {
                    println("add Invoke called and item is not contained ")

                    oldCart.copy(
                        items = this.items.apply{
                            this[item] = quantity
                        },
                        totalQuantity = this.totalQuantity + quantity,
                        subTotal = formatDoubleToString( this.subTotal.toDouble() + item.price*quantity)
                    )
                }
                else -> {
                    println("add Invoke called and this suppose not to be called")

                    oldCart
                }
            }
            updateCart(updatedCart, SavedStateKeys.Cart())

/*
            when{
                isContain(item,oldCart) -> {
                    this.totalQuantity = totalQuantity + quantity
                    this.subTotal = formatDoubleToString( this.subTotal.toDouble() + item.price*quantity)
                    this.items.apply{
                        this[item] = (this[item]?.plus(quantity)) ?: quantity
                    }
                }
                !isContain(item,oldCart) -> {
                    this.totalQuantity = totalQuantity + quantity
                    this.subTotal = formatDoubleToString( this.subTotal.toDouble() + item.price*quantity)
                    this.items.apply{
                        this[item] = quantity
                    }
                }
            }
*/

        }
/*        if(isContain(item, oldCart)) {
            updatedCart = oldCart.copy(
                items = oldCart.items.apply{

                },
                totalQuantity = oldCart.totalQuantity + quantity,
                subTotal = formatDoubleToString( oldCart.subTotal.toDouble() + item.price*quantity)
            )

        }else{
            updatedCart =  oldCart.copy(
                items = oldCart.items.apply{
                    this[item] = quantity
                },
                totalQuantity = quantity,
                subTotal = formatDoubleToString( oldCart.subTotal.toDouble() + item.price*quantity)
            )
        }*/
        return updatedCart

    }
    fun addToCart(item: SellingItem, quantity:Int = 1, oldCart:Cart): Cart {

        val updatedCart:Cart
        if(isContain(item, oldCart)) {
            updatedCart = oldCart.copy(
                items = oldCart.items.mapValues {
                        (item.quantity + quantity)
                }.toMutableMap(),
                totalQuantity = oldCart.totalQuantity + quantity,
                subTotal = formatDoubleToString( oldCart.subTotal.toDouble() + item.price*quantity)
            )

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
        return fromData.items.containsKey(selectedItem)
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
