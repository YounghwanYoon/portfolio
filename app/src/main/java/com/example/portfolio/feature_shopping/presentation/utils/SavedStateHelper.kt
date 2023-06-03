package com.example.portfolio.feature_shopping.presentation.utils

import androidx.lifecycle.SavedStateHandle
import com.example.portfolio.feature_shopping.domain.model.Cart
import com.example.portfolio.feature_shopping.domain.model.User

object SavedStateHelper {

    fun updateSavedState(target: SavedStateKeys, data: Any) {
        val savedState = SavedStateHandle()
        when (target) {
            is SavedStateKeys.Cart -> {
                savedState[target.key] = data
            }
            is SavedStateKeys.Payment -> {
                savedState[target.key] = data
            }
            is SavedStateKeys.User -> {
                savedState[target.key] = data
            }
            is SavedStateKeys.SelectedItem -> {
                savedState[target.key] = data
            }
            is SavedStateKeys.SellingItem -> {
                savedState[target.key] = data
            }
            is SavedStateKeys.SpecialItem -> {
                savedState[target.key] = data
            }
        }
    }

    fun getSavedState(target: SavedStateKeys): Any {
        val savedState = SavedStateHandle()
        when (target) {
            is SavedStateKeys.Cart -> {
                //SavedStateHandle().get<Cart>(SAVEDSTATEKEYS.CART) ?: Cart()
                return savedState.get<Cart>(target.key) ?: Cart()
            }
            is SavedStateKeys.Payment -> {
                //return savedState.get<Payment>(target.key) ?: Cart()
            }
            is SavedStateKeys.User -> {
                return savedState.get<User>(target.key) ?: User()
            }
            else -> {
                return Unit
            }
        }
        return Unit
    }
}