package com.example.portfolio.feature_shopping.presentation.payment

import android.os.Environment
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.feature_shopping.domain.model.User
import com.example.portfolio.feature_shopping.domain.use_case.PaymentUseCases
import com.example.portfolio.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentUseCases: PaymentUseCases
) : ViewModel(){

    private var _userInfo: MutableState<User> = mutableStateOf(User())
    var userInfo: State<User> = _userInfo

    private var _openDialogState:MutableState<Boolean> = mutableStateOf(false)
    var openDialogState:State<Boolean> = _openDialogState

    init{
        loadUserInfo()
    }

    private fun loadUserInfo(){
        viewModelScope.launch{
            _userInfo.value = userInfo.value.copy(isLoading = true)
            val result:DataState<User> = paymentUseCases.getUserInfo()

           when(result){
              is DataState.Loading ->{}
               is DataState.Error -> {}
               is DataState.Success -> {
                   _userInfo.value = userInfo.value.copy(
                       isLoading = false
                   )
               }
           }
        }
    }

    private fun paymentEvent(event:PaymentUIEvent){
        when(event){
            is PaymentUIEvent.CompletedOrder ->{
                closeDialog()
            }
            is PaymentUIEvent.CloseDialog -> {
                closeDialog()
            }
            is PaymentUIEvent.OnBackPressed -> {
                closeDialog()
            }
            is PaymentUIEvent.OpenDialog -> {
                openDialog()
            }


        }
    }

    private fun closeDialog(){
        _openDialogState.value = false
    }
    private fun openDialog(){
        _openDialogState.value = true
    }

}

sealed class PaymentUIEvent(){
    object CompletedOrder :PaymentUIEvent()
    object CloseDialog:PaymentUIEvent()
    object OpenDialog:PaymentUIEvent()

    object OnBackPressed:PaymentUIEvent()
}
