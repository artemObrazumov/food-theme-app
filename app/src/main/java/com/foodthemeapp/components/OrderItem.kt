package com.foodthemeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodthemeapp.data.models.Order
import com.foodthemeapp.data.models.OrderStatus
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrderItem(
    order: Order,
    onOrderClick: (orderId: String) -> Unit
) {
    val format = SimpleDateFormat("dd MMMM hh:mm", Locale("ru"))
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .shadow(2.dp)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.65f)) {
            Text(
                text = order.productName.lowercase(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = Color.Black
            )
            Text(text = "Заказ")
            Text(text = format.format(order.timestamp))
            Button(onClick = { onOrderClick(order.id) }) {
                Text(text = "Отменить заказ")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = order.price,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = Color.Black
        )
    }
}