package com.example.portfolio.feature_shopping.presentation.payment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.feature_shopping.domain.model.PaymentInfo
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

}
