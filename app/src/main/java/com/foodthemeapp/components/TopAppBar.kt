package com.foodthemeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.foodthemeapp.data.Api

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    hasOrders: Boolean,
    avatarUrl: String,
    username: String,
    onLogoutClicked: () -> Unit = {},
    onCartClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatarUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Text(
            text = username
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onCartClicked
        ) {
            Box {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null
                )
                if (hasOrders) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        IconButton(
            onClick = onLogoutClicked
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun TopAppBarPreview() {
    TopAppBar(hasOrders = true, avatarUrl = "https://www.w3schools.com/w3images/avatar2.png", username = "Test")
}