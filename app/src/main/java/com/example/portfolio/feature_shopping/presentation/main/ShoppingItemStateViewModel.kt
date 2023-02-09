package com.example.portfolio.feature_shopping.presentation.main

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.model.ShoppingUIEvent
import com.example.portfolio.feature_shopping.domain.model.SpecialItem
import com.example.portfolio.feature_shopping.domain.use_case.ShoppingUseCases
import com.example.portfolio.utils.Resource
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

    init{
        viewModelScope.launch{
            loadSpecialItems()
            loadSellingItems()
        }
    }

/*
    private val specialItemForTest = listOf(
        SpecialItem(
            0,
            image = R.drawable.coffee_steam_,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = "null",
            title = "Tester 1",// title = "Tester 1" ,
            start_date = "12/21/22",
            end_date = "12/25/22"
        ),
        SpecialItem(
            id = 1,

            image = R.drawable.coffee_steam_,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = "null", title = "Tester 2", start_date ="01/01/23",end_date =  "01/01/23"
        ),
        SpecialItem(
            2, image = R.drawable.coffee_steam_,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = "null", title = "Tester 3", start_date ="05/01/23", end_date = "09/31/23"
        ),
    )
    private val regularItemTest = listOf(
        SellingItem(0, image =R.drawable.coffee_bean_falling, title = "test 1",price = 2.99, quantity =  10,
                imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""),
        SellingItem(1, image =R.drawable.coffee_bean_falling, title ="Tester 2", price = 2.99, quantity = 99,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""),
        SellingItem(2, image = R.drawable.coffee_bean_falling, title ="Tester 3", price = 2.99, quantity =  12,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""),
        SellingItem(3, R.drawable.coffee_bean_falling, title ="Tester 4", price = 2.99, quantity =  13,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""
        ),
        SellingItem(4, R.drawable.coffee_bean_falling, title ="Tester 5", price = 2.99, quantity =  14,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""),
        SellingItem(5, R.drawable.coffee_bean_falling, title ="Tester 6", price = 2.99, quantity =  15,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""),
        SellingItem(6, R.drawable.coffee_bean_falling, title ="Tester 7", price = 2.99, quantity =  16,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""),
        SellingItem(7, R.drawable.coffee_bean_falling, title ="Tester 8", price = 2.99, quantity =  17,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""),
        SellingItem(8, R.drawable.coffee_bean_falling, title ="Tester 9", price = 2.99, quantity =  18,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""),
        SellingItem(9, R.drawable.coffee_bean_falling, title ="Tester 10", price = 2.99, quantity =  19,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""),
        SellingItem(10, R.drawable.coffee_bean_falling, title ="Tester 11", price = 2.99, quantity = 20,
            imageUrl = "https://pixabay.com/get/gc662d40d472a4a783a906f58f21e4cdb98c02d9c30bb80a33f34510c2b5462fae23eea3a4e3d37fe321805a2effed8e5_640.jpg",
            description = ""),
    )
*/
/*


    //private val _SpecialListState = MutableStateFlow<List<SpecialItem>>(emptyList())
   // var specialItems = savedStateHandle.getStateFlow(key = KEY_SPECIAL_ITEM, initialValue = emptyList<SpecialItem>())// emptyList<SpecialItem>())
    var specialItems = savedStateHandle.getStateFlow(key = KEY_SPECIAL_ITEM, initialValue = emptyList<SpecialItem>()) // emptyList<SpecialItem>())
        private set
    //var regularItems = savedStateHandle.getStateFlow(key = KEY_SELLING_ITEM, initialValue = emptyList<SellingItem>())
    var sellingItems = savedStateHandle.getStateFlow(key = KEY_SELLING_ITEM, initialValue = emptyList<SellingItem>())
        private set
*/


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
                        savedStateHandle.set<List<SpecialItem>>(KEY_SPECIAL_ITEM, it)
                    }
                    _specialItems.value = it.data!!

                    _specialItemState.value = it
                    //_SpecialListState.value = it
                    Log.d(TAG, "getSpecial: data from Viewmodel is ${it.data?.get(0)?.imageUrl}}")



                    //savedStateHandle[KEY_SPECIAL_ITEM] = it.data
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
                    savedStateHandle[KEY_SELLING_ITEM] = it.data
                    _sellingItems.value = it.data!!

                    for(item in savedStateHandle.get<List<SellingItem>>(KEY_SELLING_ITEM)!!){
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

}

sealed class ItemUiState<T>{
    data class Success<T>(val items: List<T>):ItemUiState<T>()
    data class Loading<T>(val items:List<T> = emptyList<T>()):ItemUiState<T>()
    data class Error (val exception:Throwable):ItemUiState<Nothing>()
}

const val KEY_SPECIAL_ITEM = "specialItem"
const val KEY_SELLING_ITEM = "sellingItem"