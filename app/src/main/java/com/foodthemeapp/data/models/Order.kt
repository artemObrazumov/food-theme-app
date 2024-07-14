package com.foodthemeapp.data.models

data class Order(
    val id: String = "",
    val productId: String = "",
    val timestamp: Long = 0L,
    val productName: String = "",
    val price: String = ""
)