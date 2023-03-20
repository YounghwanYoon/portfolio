package com.example.portfolio.feature_shopping.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.feature_shopping.domain.model.SellingItem
import com.example.portfolio.feature_shopping.presentation.search.util.SearchBarState
import com.example.portfolio.utils.ConstKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
):ViewModel() {

    private val _searchBarState = mutableStateOf(value = SearchBarState.CLOSED)
    var searchBarState: State<SearchBarState> = _searchBarState

    private val _searchTextState = MutableStateFlow(value = "")
    var searchTextState: StateFlow<String> = _searchTextState

    private val _isSearching = MutableStateFlow(value = false)
    val isSearhing = _isSearching.asStateFlow()

    private var _matchedItems = MutableStateFlow(savedStateHandle.get<List<SellingItem>>(ConstKeys.SELLING_ITEM) ?: emptyList())
    val matchedItems: StateFlow<List<SellingItem>> = _matchedItems.asStateFlow()

    val items = searchTextState
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







    fun updateSearchState(updatedValue: SearchBarState){
        _searchBarState.value = updatedValue
    }
    fun updateSearchTextState(updatedValue:String){
        _searchTextState.value = updatedValue
    }

}