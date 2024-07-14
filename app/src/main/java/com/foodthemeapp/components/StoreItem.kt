package com.foodthemeapp.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.foodthemeapp.data.models.Store
import com.foodthemeapp.ui.theme.Colors

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.StoreItem(
    store: Store,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onItemClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { onItemClicked() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        with(sharedTransitionScope) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(store.icon)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "store_icon${store.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)

            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .sharedElement(
                    rememberSharedContentState(key = "store_name${store.id}"),
                    animatedVisibilityScope = animatedVisibilityScope
                ),
            text = store.name,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
    }
}