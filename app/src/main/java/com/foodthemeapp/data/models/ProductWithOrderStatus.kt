package com.foodthemeapp.data.models

data class ProductWithOrderStatus(
    val product: Product,
    val orderStatus: OrderStatus,
    val orderId: String = ""
)

enum class OrderStatus {
    NotOrdered, Ordered, Ordering
}