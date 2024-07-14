package com.foodthemeapp.screen.main.orders

import com.foodthemeapp.data.models.Order

sealed class OrdersScreenState {

    data object Idle: OrdersScreenState()
    data object Loading: OrdersScreenState()
    data class Content(
        val orders: List<Order>
    ): OrdersScreenState()
}
