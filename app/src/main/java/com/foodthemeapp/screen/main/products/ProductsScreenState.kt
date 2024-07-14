package com.foodthemeapp.screen.main.products

import com.foodthemeapp.data.models.ProductWithOrderStatus

sealed class ProductsScreenState {
    data class Content(
        val products: List<ProductWithOrderStatus>
    ): ProductsScreenState()
    data object Loading: ProductsScreenState()
    data object Idle: ProductsScreenState()
}

