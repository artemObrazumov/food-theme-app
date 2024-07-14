package com.foodthemeapp.screen.main.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.foodthemeapp.FoodApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrdersScreenViewModel(private val userId: String): ViewModel() {

    private val _state: MutableStateFlow<OrdersScreenState> =
        MutableStateFlow(OrdersScreenState.Idle)
    val state = _state.asStateFlow()

    init {
        if (state.value is OrdersScreenState.Idle) {
            loadOrders()
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _state.update { OrdersScreenState.Loading }
            val orders = FoodApp.api.getOrders(userId = userId)
            _state.update {
                OrdersScreenState.Content(
                    orders = orders
                )
            }
        }
    }

    fun cancelOrder(orderId: String) {
        viewModelScope.launch {
            val orders = (_state.value as OrdersScreenState.Content).orders.toMutableList()
            val itemToReplace = orders.find { it.id == orderId }!!

            FoodApp.api.deleteOrder(
                userId = FoodApp.googleAuthUiClient.getSignedInUser()!!.userId,
                orderId = orderId
            )
            orders.removeAt(orders.indexOf(itemToReplace))
            _state.update {
                (_state.value as OrdersScreenState.Content).copy(
                    orders = orders
                )
            }
        }
    }
}

class OrdersScreenViewModelFactory(
    private val userId: String
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        OrdersScreenViewModel(userId) as T
}