package com.example.portfolio.feature_shopping.presentation.main

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.ShoppingUIEvent
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.use_case.ShoppingUseCases
import com.example.portfolio.feature_shopping.presentation.search.util.SearchBarState
import com.example.portfolio.utils.Resource
import com.example.portfolio.utils.ConstKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingItemStateViewModel @Inject constructor(
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
    private val _specialItems = MutableStateFlow<List<SpecialItem>>(emptyList())
    val specialItems = _specialItems.asStateFlow()

    private val _sellingItems = MutableStateFlow<List<SellingItem>>(emptyList())
    val sellingItems = _sellingItems.asStateFlow()

    private val _specialItemState = MutableStateFlow<Resource<List<SpecialItem>>>(Resource.Loading(emptyList()))
    val specialItemState = _specialItemState.asStateFlow()

    private val _sellingItemState = MutableStateFlow<Resource<List<SellingItem>>>(Resource.Loading(emptyList()))
    val sellingItemState = _sellingItemState.asStateFlow()

    val selectedItem = savedStateHandle.getStateFlow<SellingItem?>(ConstKeys.SELECTED_ITEM, null)

    init{
        viewModelScope.launch{
            loadSpecialItems()
            loadSellingItems()
        }
    }

    fun onUIEvent(event: ShoppingUIEvent){
        Log.d(TAG, "onUIEvent: is called")
        when(event){
            ShoppingUIEvent.AppLaunched -> {
                Log.d(TAG, "onUIEvent: AppLaunched Event is called")
                viewModelScope.launch{
                    launch{
                        loadSpecialItems()
                    }
                    launch{
                        loadSellingItems()
                    }
                }
            }
            else ->{
                //something went wrong.
            }
        }
    }

    private suspend fun loadSpecialItems(){

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

                    it.data?.let{
                        savedStateHandle.set<List<SpecialItem>>(ConstKeys.SELLING_ITEM, it)
                    }
                    _specialItems.value = it.data!!

                    _specialItemState.value = it
                    //_SpecialListState.value = it
                    Log.d(TAG, "getSpecial: data from Viewmodel is ${it.data?.get(0)?.imageUrl}}")



                    //savedStateHandle[ConstKeys.ConstKeys.SELLING_ITEM] = it.data
                }
            }

        }
    }
    private suspend fun loadSellingItems(){
        shoppingUseCases.getRegular().collect{
            when(it){
                is Resource.Error -> {
                    Log.d(TAG, "onEvent - getRegular: Error collected from UseCase.getSpecial ")
                }
                is Resource.Loading -> {
                    Log.d(TAG, "onEvent - getRegular: Loading Started")
                }
                is Resource.Success -> {
                    Log.d(TAG, "onEvent - getRegular: Success collected from UseCase.getSpecial")
                    savedStateHandle[ConstKeys.SELLING_ITEM] = it.data
                    _sellingItems.value = it.data!!

                    for(item in savedStateHandle.get<List<SellingItem>>(ConstKeys.SELLING_ITEM)!!){
                        Log.d(TAG, "getRegular: updated item ${item.imageUrl}")
                    }
                    _sellingItemState.value = it
                }
            }
        }

    }

    fun getSelectedItem(itemId:Int):SellingItem{
        return sellingItems.value[itemId]
    }
    fun setSelectedItem(item: SellingItem){
        savedStateHandle[ConstKeys.SELECTED_ITEM] = item
    }

}