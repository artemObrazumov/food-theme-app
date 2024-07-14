package com.foodthemeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.foodthemeapp.data.models.OrderStatus
import com.foodthemeapp.data.models.Product

@Composable
fun ProductItem(
    product: Product,
    orderStatus: OrderStatus,
    onOrderClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .shadow(2.dp)
            .padding(8.dp)
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.thumb)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Column(
                modifier = Modifier.fillMaxWidth(0.65f)
            ) {
                Text(
                    text = product.name.lowercase(),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color.Black
                )
                Text(
                    text = product.desc,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onOrderClick() }) {
                    Text(
                        text = when (orderStatus) {
                            OrderStatus.Ordered -> {
                                "Заказано"
                            }

                            OrderStatus.Ordering -> {
                                "Заказывается"
                            }

                            else -> {
                                "Не заказано"
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = product.price,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = Color.Black
            )
        }
    }
}