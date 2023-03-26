package com.example.portfolio.feature_shopping.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.domain.use_case.GetItemsFromLocalDB
import com.example.portfolio.feature_shopping.presentation.search.util.SearchBarState
import com.example.portfolio.utils.ConstKeys
import com.example.portfolio.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getLocalData: GetItemsFromLocalDB
):ViewModel() {

    private val _searchBarState = mutableStateOf(value = SearchBarState.CLOSED)
    var searchBarState: State<SearchBarState> = _searchBarState

    private val _searchTextState = MutableStateFlow(value = "")
    var searchTextState: StateFlow<String> = _searchTextState

    private val _isSearching = MutableStateFlow(value = false)
    val isSearching = _isSearching.asStateFlow()

    //private val allData = MutableStateFlow<List<SellingItem>>(emptyList())

    private var _matchedItems:MutableStateFlow<List<SellingItem>> = MutableStateFlow(emptyList())
    //val matchedItems: StateFlow<List<SellingItem>> = _matchedItems.asStateFlow()

    val  matchedItems:StateFlow<List<SellingItem>> = searchTextState
        // this will help wait user's type until 1 sec
        .debounce(1000L)
        .onEach{ _isSearching.update {true}}
        .combine(_matchedItems){ searchText, items ->
            if(searchText.isBlank()) {
                items
            }else{
                delay(2000L)
                items.filter{
                    it.doesMatchSearchQuery(searchText)
                }
            }
        }
        //once done
        .onEach{_isSearching.update{false}}
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _matchedItems.value
        )


    init{
        getData()
    }

    private fun getData(){
        viewModelScope.launch{
            getLocalData().collect{
                when(it){
                    is Resource.Error -> {
                        println("Error from ROOM")
                    }
                    is Resource.Loading -> {
                        println("Loading from ROOM")
                    }
                    is Resource.Success -> {
                        println("Success from ROOM")
                        println(it.data!!.size)
                        _matchedItems.value = it.data ?: emptyList()
                        //allData. value = it.data
                        //_matchedItems = allData
                    }
                }
            }
        }
    }

    fun updateSearchState(updatedValue: SearchBarState){
        _searchBarState.value = updatedValue
    }
    fun updateSearchTextState(updatedValue:String){
        _searchTextState.value = updatedValue
        //loadItems()
    }
    fun getSelectedItem(itemId:Int):SellingItem?{
        val selectedItem:SellingItem? = _matchedItems.value.find{
            it.id == itemId
        }
        return selectedItem
    }

}