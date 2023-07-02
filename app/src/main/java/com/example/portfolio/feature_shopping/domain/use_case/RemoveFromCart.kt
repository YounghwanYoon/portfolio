package com.example.portfolio.feature_shopping.domain.use_case

import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.repository.webservices.ShoppingRepository
import com.example.portfolio.feature_shopping.presentation.utils.Helper.formatDoubleToString
import com.example.portfolio.feature_shopping.presentation.utils.Helper.updateCart
import com.example.portfolio.feature_shopping.presentation.utils.SavedStateKeys
import timber.log.Timber
import javax.inject.Inject

class RemoveReduceFromCart @Inject constructor(
    private val _repository: ShoppingRepository,
) {

    fun removeAllItem():Cart{
        val updatedCart = Cart()
        updateCart(updatedCart, SavedStateKeys.Cart())

        return updatedCart
    }
    fun removeItem(item: SellingItem, oldCart: Cart):Cart{

        Timber.d("removeItem(): Current totalQuantity = ${oldCart.totalQuantity}\n" +
                "subtotal ${oldCart.subTotal}\n" +
                "item is ${item}")

        val newTotalQuantity = oldCart.totalQuantity - oldCart.items[item]!!
        val newSubTotal = formatDoubleToString((oldCart.subTotal.toDouble() - (item.price * oldCart.items[item]!!)))
        oldCart.items.remove(item)
        val newItems =oldCart.items

        val updatedCart  = oldCart.copy(
            totalQuantity = newTotalQuantity,//(curTotalQuantity - itemQuantity),
            subTotal =  newSubTotal,
            items = newItems
        )

        Timber.d("After removeItem(): Current totalQuantity = ${oldCart.totalQuantity}\n" +
                "subtotal ${oldCart.subTotal}\n" +
                "item is ${item}")

        updateCart(updatedCart, SavedStateKeys.Cart())
        return updatedCart
    }

    fun reduceItem(item: SellingItem, reduceAmount:Int = 1, oldCart: Cart):Cart{
        Timber.d( "reduceItem: is called")

        val itemQuantity = oldCart.items[item]


        if (itemQuantity != null) {
            if(itemQuantity <= reduceAmount) return oldCart
        }

        var updatedCart:Cart
        oldCart.apply{
            updatedCart = when{
                (this.items[item]!! > 1) -> {
                    Timber.d( "reducing")

                    oldCart.copy(
                        items = this.items.apply{
                            this[item] = this[item]!!.minus(reduceAmount)
                        },
                        totalQuantity = (this.totalQuantity - reduceAmount),
                        subTotal = formatDoubleToString(this.subTotal.toDouble() - item.price * reduceAmount)
                    )
                }

                else -> {
                    Timber.d("reduceItem() is called and this suppose not to be called")

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
                SellingItem = oldCart.SellingItem.mapValues {   (key, value) ->
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
