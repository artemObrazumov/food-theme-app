package com.foodthemeapp.screen.main.orders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodthemeapp.components.OrderItem

@Composable
fun OrdersScreen(
    state: OrdersScreenState,
    onCancelOrder: (orderId: String) -> Unit
) {
    when(state) {
        is OrdersScreenState.Content -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 8.dp
                    )
            ) {
                Text(
                    text = "Заказано:",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = Color.Black
                )
                LazyColumn {
                    items(
                        items = state.orders
                    ) {
                        OrderItem(
                            order = it,
                            onOrderClick = { orderId ->
                                onCancelOrder(orderId)
                            }
                        )
                    }
                }
            }
        } else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}