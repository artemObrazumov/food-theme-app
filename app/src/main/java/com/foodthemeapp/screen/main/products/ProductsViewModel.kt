package com.foodthemeapp.screen.main.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.foodthemeapp.FoodApp
import com.foodthemeapp.data.models.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val storeId: String,
    private val userId: String
): ViewModel() {

    private val _state: MutableStateFlow<ProductsScreenState> =
        MutableStateFlow(ProductsScreenState.Idle)
    val state = _state.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            _state.update {
                ProductsScreenState.Loading
            }
            val products = FoodApp.api.getProducts(storeId, userId)
            _state.update {
                ProductsScreenState.Content(products = products)
            }
        }
    }

    fun onProductOrdered(productId: String) {
        viewModelScope.launch {

            val products = (_state.value as ProductsScreenState.Content).products.toMutableList()
            val itemToReplace = products.find { it.product.id == productId }!!
            val shouldOrder = products[products.indexOf(itemToReplace)].orderStatus == OrderStatus.NotOrdered

            if (shouldOrder) {
                val orderId = FoodApp.api.orderProduct(
                    userId = FoodApp.googleAuthUiClient.getSignedInUser()!!.userId,
                    id = itemToReplace.product.id,
                    productName = itemToReplace.product.name,
                    price = itemToReplace.product.price,
                )
                products[products.indexOf(itemToReplace)] =
                    itemToReplace.copy(orderStatus = OrderStatus.Ordered,
                        orderId = orderId)
            } else {
                FoodApp.api.deleteOrder(
                    userId = FoodApp.googleAuthUiClient.getSignedInUser()!!.userId,
                    orderId = itemToReplace.orderId
                )
                products[products.indexOf(itemToReplace)] =
                    itemToReplace.copy(orderStatus = OrderStatus.NotOrdered)
            }

            _state.update {
                (_state.value as ProductsScreenState.Content).copy(
                    products = products
                )
            }
        }
    }
}

class ProductsScreenViewModelFactory(
    private val storeId: String,
    private val userId: String
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ProductsViewModel(storeId, userId) as T
}