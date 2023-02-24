package com.example.portfolio.feature_shopping.domain.use_case

import androidx.lifecycle.SavedStateHandle
import com.example.portfolio.feature_shopping.domain.model.User
import com.example.portfolio.utils.DataState
import com.example.portfolio.utils.SAVEDSTATEKEYS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetUserInfo(
    val savedStateHandle: SavedStateHandle = SavedStateHandle()
) {

    operator fun invoke(): DataState<User> = DataState.Success(User())

}