package com.example.portfolio.feature_shopping.presentation.main

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.R
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.ShoppingUIEvent
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.use_case.*
import com.example.portfolio.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val shoppingUseCases:ShoppingUseCases
    /*=
        ShoppingUseCases(
            getSpecial = GetSpecialItem(
                repository = tempRepositoryExt()
            ),
            getRegular = GetRegularItem(
                repository =tempRepositoryExt()
            ),
            addToCart = AddToCart(repository =tempRepositoryExt()),

            removeCart = RemoveFromCart(
                repository =tempRepositoryExt()
            ),
        ),*/
): ViewModel() {
    private val TAG = this.javaClass.name

    private val specialItemForTest = listOf(
        SpecialItem(0, R.drawable.coffee_steam_, "null","Tester 1", title = "Tester 1" ,"12/21/22", "12/25/22"),
        SpecialItem(1, R.drawable.coffee_steam_, "null", "Tester 2", title = "Tester 1" ,"01/01/23", "01/01/23"),
        SpecialItem(2, R.drawable.coffee_steam_, "null", "Tester 3", title = "Tester 1" ,"05/01/23", "09/31/23"),
    )
    private val regularItemTest = listOf(
        SellingItem(0, R.drawable.coffee_bean_falling, title = "test 1",price = 2.99, quantity =  10),
        SellingItem(1, R.drawable.coffee_bean_falling, title ="Tester 2", price = 2.99, quantity = 99),
        SellingItem(2, R.drawable.coffee_bean_falling, title ="Tester 3", price = 2.99, quantity =  12),
        SellingItem(3, R.drawable.coffee_bean_falling, title ="Tester 4", price = 2.99, quantity =  13),
        SellingItem(4, R.drawable.coffee_bean_falling, title ="Tester 5", price = 2.99, quantity =  14),
        SellingItem(5, R.drawable.coffee_bean_falling, title ="Tester 6", price = 2.99, quantity =  15),
        SellingItem(6, R.drawable.coffee_bean_falling, title ="Tester 7", price = 2.99, quantity =  16),
        SellingItem(7, R.drawable.coffee_bean_falling, title ="Tester 8", price = 2.99, quantity =  17),
        SellingItem(8, R.drawable.coffee_bean_falling, title ="Tester 9", price = 2.99, quantity =  18),
        SellingItem(9, R.drawable.coffee_bean_falling, title ="Tester 10", price = 2.99, quantity =  19),
        SellingItem(10, R.drawable.coffee_bean_falling, title ="Tester 11", price = 2.99, quantity = 20),
    )
/*

    private val specialItemForTest = listOf(
        SpecialItem(0, R.drawable.coffee_steam_, "null","Tester 1", title = "Tester 1" ,"12/21/22", "12/25/22"),
        SpecialItem(1, R.drawable.coffee_steam_, "null", "Tester 2", title = "Tester 1" ,"01/01/23", "01/01/23"),
        SpecialItem(2, R.drawable.coffee_steam_, "null", "Tester 3", title = "Tester 1" ,"05/01/23", "09/31/23"),
    )
    private val regularItemTest = listOf(
        SellingItem(0, R.drawable.coffee_bean_falling, title = "test 1",price = 2.99, quantity =  10),
        SellingItem(1, R.drawable.coffee_bean_falling, title ="Tester 2", price = 2.99, quantity = 99),
        SellingItem(2, R.drawable.coffee_bean_falling, title ="Tester 3", price = 2.99, quantity =  12),
        SellingItem(3, R.drawable.coffee_bean_falling, title ="Tester 4", price = 2.99, quantity =  13),
        SellingItem(4, R.drawable.coffee_bean_falling, title ="Tester 5", price = 2.99, quantity =  14),
        SellingItem(5, R.drawable.coffee_bean_falling, title ="Tester 6", price = 2.99, quantity =  15),
        SellingItem(6, R.drawable.coffee_bean_falling, title ="Tester 7", price = 2.99, quantity =  16),
        SellingItem(7, R.drawable.coffee_bean_falling, title ="Tester 8", price = 2.99, quantity =  17),
        SellingItem(8, R.drawable.coffee_bean_falling, title ="Tester 9", price = 2.99, quantity =  18),
        SellingItem(9, R.drawable.coffee_bean_falling, title ="Tester 10", price = 2.99, quantity =  19),
        SellingItem(10, R.drawable.coffee_bean_falling, title ="Tester 11", price = 2.99, quantity = 20),
    )
*/


    //private val _SpecialListState = MutableStateFlow<List<SpecialItem>>(emptyList())
    val specialItemsState = savedStateHandle.getStateFlow(key = "specialListStat", initialValue = specialItemForTest)// emptyList<SpecialItem>())
    val regularItemsState = savedStateHandle.getStateFlow(key = "regularItemsState", initialValue =regularItemTest, )

/*
    suspend operator fun invoke(){
        onUIEvent(ShoppingUIEvent.AppLaunched)
    }
    */

    fun onUIEvent(event: ShoppingUIEvent){
        when(event){
            ShoppingUIEvent.AppLaunched -> {
                viewModelScope.launch{
                    launch{
                        getSpecial(shoppingUseCases)
                    }
                    launch{
                        getRegular(shoppingUseCases)
                    }
                }
            }
            else ->{
                //something went wrong.
            }

        }
    }

    private suspend fun getSpecial(shoppingUseCase:ShoppingUseCases){
        shoppingUseCases.getSpecial().collect{
            when(it){
                is Resource.Error -> {
                    Log.d(TAG, "onEvent: Error collected from UseCase.getSpecial ")
                }
                is Resource.Loading -> {
                    Log.d(TAG, "onEvent: Loading Started")
                }
                is Resource.Success -> {
                    Log.d(TAG, "onEvent: Success collected from UseCase.getSpecial")
                    //_SpecialListState.value = it
                    savedStateHandle["specialListState"] = it.data
                }
            }

        }
    }
    private suspend fun getRegular(shoppingUseCase: ShoppingUseCases){
        shoppingUseCases.getRegular().collect{
            when(it){
                is Resource.Error -> {
                    Log.d(TAG, "onEvent: Error collected from UseCase.getSpecial ")
                }
                is Resource.Loading -> {
                    Log.d(TAG, "onEvent: Loading Started")
                }
                is Resource.Success -> {
                    Log.d(TAG, "onEvent: Success collected from UseCase.getSpecial")

                    savedStateHandle["regularItemsState"] = it.data
                }
            }
        }

    }

}