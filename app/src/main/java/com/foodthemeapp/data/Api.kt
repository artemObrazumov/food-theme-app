package com.foodthemeapp.data

import com.foodthemeapp.data.models.Order
import com.foodthemeapp.data.models.OrderStatus
import com.foodthemeapp.data.models.Product
import com.foodthemeapp.data.models.ProductWithOrderStatus
import com.foodthemeapp.data.models.Store
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.UUID

class Api {

    suspend fun getProducts(
        storeId: String,
        userId: String,
    ): List<ProductWithOrderStatus> {
        val products = (FirebaseDatabase.getInstance()
            .getReference("products")
            .get().await()
            .getValue<List<Product>>() ?: emptyList())
            .filter { it.store == storeId }
        val productsWithStatus = mutableListOf<ProductWithOrderStatus>()
        val orders = FirebaseDatabase.getInstance()
            .getReference("userOrders/$userId")
            .get().await()
            .getValue<Map<String,Order>>() ?: emptyMap()
        val orderedProducts = orders.map { it.value.productId }

        products.forEach { product ->
            productsWithStatus.add(
                ProductWithOrderStatus(
                    product = product,
                    orderStatus = if (orderedProducts.contains(product.id)) OrderStatus.Ordered
                    else OrderStatus.NotOrdered,
                    orderId = if (orderedProducts.contains(product.id)) orders.values.find { it.productId == product.id }!!.id else ""
                )
            )
        }
        return productsWithStatus
    }

    suspend fun getStores(): List<Store> =
        FirebaseDatabase.getInstance()
            .getReference("restaurants")
            .get().await()
            .getValue<List<Store>>() ?: emptyList()

    fun orderProduct(
        userId: String,
        id: String,
        productName: String,
        price: String
    ): String {
        val orderId = UUID.randomUUID().toString()
        FirebaseDatabase.getInstance()
            .getReference("userOrders/$userId/$orderId")
            .setValue(
                Order(
                    id = orderId,
                    productId = id,
                    timestamp = Calendar.getInstance().timeInMillis,
                    productName = productName,
                    price = price
                )
            )
        return orderId
    }

    suspend fun deleteOrder(userId: String, orderId: String) {
        FirebaseDatabase.getInstance()
            .getReference("userOrders/$userId/$orderId")
            .removeValue()
    }

    suspend fun getOrders(userId: String): List<Order> =
        (FirebaseDatabase.getInstance()
            .getReference("userOrders/$userId")
            .get().await().getValue<Map<String, Order>>() ?: emptyMap())
            .map { it.value }
}