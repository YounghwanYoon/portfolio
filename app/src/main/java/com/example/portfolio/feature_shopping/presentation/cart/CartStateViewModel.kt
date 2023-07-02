package com.example.portfolio.feature_shopping.presentation.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.use_case.AddToCart
import com.example.portfolio.feature_shopping.domain.use_case.GetCart
import com.example.portfolio.feature_shopping.domain.use_case.RemoveReduceFromCart
import com.example.portfolio.feature_shopping.presentation.utils.CartUIEvent
import com.example.portfolio.utils.ConstKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class CartStateViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCart: GetCart,
    private val addToCart: AddToCart,
    private val removeReduceFromCart: RemoveReduceFromCart,
): ViewModel() {
    private val TAG = this.javaClass.name
    private var _cartUIState = MutableStateFlow(getCart())
/*    var cartUIState = _cartUIState.asStateFlow()
        private set*/
    var cartUIState by  mutableStateOf<Cart>(getCart())//savedStateHandle.get<Cart>(SAVEDSTATEKEYS.CART) ?: Cart())
        private set

    var subTotal by mutableStateOf<String>(cartUIState.subTotal)
        private set

    var totalQuantity by mutableStateOf<Int>(cartUIState.totalQuantity)
        private set

    private fun updateCart(updatedState:Cart) {
        savedStateHandle[ConstKeys.CART] = updatedState
    }
    private fun formatHelper(value:Double):String{
        val decimalFormatter = DecimalFormat("#.##")
        decimalFormatter.roundingMode = RoundingMode.HALF_UP

        return decimalFormatter.format(value)//.toDouble()
    }

    fun setCartUIEvent(event : CartUIEvent){
        when(event){
            is CartUIEvent.AddToCart -> {
                Timber.d("onAddEvent is triggered")
                //addItem(item = event.selectedItem, quantity = event.quantity)
                cartUIState = addToCart(event.selectedItem, event.quantity, this.cartUIState)

            }
            is CartUIEvent.ReduceFromCart -> {

                Timber.d("onReduceEvent is triggered")
                cartUIState = removeReduceFromCart.reduceItem(
                    item = event.selectedItem,
                    reduceAmount = event.quantity,
                    oldCart = cartUIState
                )
                //updateCart(cartUIState)
                //reduceItem(selectedItem = event.selectedItem, quantity = event.quantity)
            }
            is CartUIEvent.RemoveFromCart -> {
                cartUIState = removeReduceFromCart.removeItem(
                    item = event.selectedItem,
                    oldCart = cartUIState
                )

                Timber.d("onRemoveEvent is triggered")
            }

            is CartUIEvent.RemoveAllFromCart -> {
                Timber.d("onRemoveAllEvent is triggered")
                cartUIState = Cart()
                updateCart(cartUIState)
            }
            else -> {
                Timber.d("From CartStateViewModel: this should not be called ")
            }
        }
    }
}


