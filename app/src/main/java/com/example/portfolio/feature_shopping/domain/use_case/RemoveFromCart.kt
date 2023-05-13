package com.example.portfolio.feature_shopping.domain.use_case

import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.feature_shopping.presentation.utils.Helper.formatDoubleToString
import com.example.portfolio.feature_shopping.presentation.utils.Helper.updateCart
import com.example.portfolio.feature_shopping.presentation.utils.SavedStateKeys
import javax.inject.Inject

class RemoveReduceFromCart @Inject constructor(
    private val _repository: ShoppingRepository,
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
/*
    private var _cartData: Cart =  SavedStateHandle().get<Cart>(ConstKeys.CART) ?: Cart()

    fun removeAllItem(){
        _cartData = Cart()
        updateCart()
    }
    fun removeItem(selectedItem: SellingItem):Boolean{
        println( "reduceItem: is called")
        _cartData.apply{
            this.totalQuantity -= 1
            this.subTotal = "0.00"
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
                    cart.subTotal = (cart.subTotal.toDouble() - selectedItem.price).toString()
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
    }*/
    fun removeAllItem():Cart{
        val updatedCart = Cart()
        updateCart(updatedCart, SavedStateKeys.Cart())

        return updatedCart
    }
    fun removeItem(item: SellingItem, oldCart: Cart):Cart{
        println( "reduceItem: is called")
/*
        val newCart = oldCart.apply{
            this.totalQuantity -= 1
            this.subTotal = "0.00"
            this.items.remove(item)
        }
*/
        val updatedCart  = oldCart.copy(
            totalQuantity = (oldCart.totalQuantity - item.quantity),
            subTotal =  formatDoubleToString(oldCart.subTotal.toDouble() - (item.quantity * item.price)),
            items = oldCart.items.apply{
                this.remove(item)
            }
        )

        updateCart(updatedCart, SavedStateKeys.Cart())
        return updatedCart
    }

    fun reduceItem(item: SellingItem, reduceAmount:Int = 1, oldCart: Cart):Cart{
        println( "reduceItem: is called")

        var updatedCart:Cart
        oldCart.apply{
            updatedCart = when{
                (item.quantity > 1) -> {
                    oldCart.copy(
                        items = this.items.apply{
                            this[item] = this[item]!!.minus(reduceAmount)
                        },
                        totalQuantity = (this.totalQuantity - reduceAmount),
                        subTotal = formatDoubleToString(this.subTotal.toDouble() - item.price * reduceAmount)
                    )
                }

                else -> {
                    println("reduceItem() is called and this suppose not to be called")

                    oldCart
                }
            }
            updateCart(updatedCart, SavedStateKeys.Cart())
        }
/*
        if(item.quantity > 2){
            val updatedCart = oldCart.copy(
                totalQuantity = (oldCart.totalQuantity - item.quantity),
                subTotal =  Helper.formatDoubleToString(oldCart.subTotal.toDouble() - (item.quantity * item.price)),
                items = oldCart.items.mapValues {   (key, value) ->
                   value-reduceAmount
                }.toMutableMap()
            )
            updateCart(updatedCart, SavedStateKeys.Cart() )
            return updatedCart
        }*/
        return updatedCart
        //if smaller than 2 : 1 or 0 ignore since there is remove button to actually delete from the cart.
    }


}
