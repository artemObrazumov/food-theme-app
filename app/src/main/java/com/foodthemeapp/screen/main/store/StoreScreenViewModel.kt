package com.foodthemeapp.screen.main.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodthemeapp.FoodApp
import com.foodthemeapp.screen.main.orders.OrdersScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreScreenViewModel: ViewModel() {

    private val _state: MutableStateFlow<StoreScreenState> =
        MutableStateFlow(StoreScreenState.Idle)
    val state = _state.asStateFlow()

    init {
        if (_state.value is StoreScreenState.Idle) {
            loadStores()
        }
    }

    private fun loadStores() {
        viewModelScope.launch {
            _state.update {
                StoreScreenState.Loading
            }
            val stores = FoodApp.api.getStores()
            _state.update {
                StoreScreenState.Content(stores = stores)
            }
        }
    }
}