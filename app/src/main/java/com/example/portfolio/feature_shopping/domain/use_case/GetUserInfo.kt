package com.example.portfolio.feature_shopping.domain.use_case

import androidx.lifecycle.SavedStateHandle
import com.example.portfolio.feature_shopping.domain.model.User
import com.example.portfolio.utils.DataState

class GetUserInfo(
    val savedStateHandle: SavedStateHandle = SavedStateHandle()
) {

    operator fun invoke(): DataState<User> = DataState.Success(User())

}