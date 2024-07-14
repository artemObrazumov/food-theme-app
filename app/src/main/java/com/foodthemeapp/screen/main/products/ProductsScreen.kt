package com.foodthemeapp.screen.main.products

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodthemeapp.components.ProductItem
import com.foodthemeapp.components.StorePageHeader
import com.foodthemeapp.data.models.Store

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProductsScreen(
    store: Store,
    state: ProductsScreenState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onGoBack: () -> Unit,
    onOrderedItem: (id: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp
            )
    ) {
        StorePageHeader(
            store = store,
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope,
            onGoBack = { onGoBack() }
        )
        when (state) {
            is ProductsScreenState.Content -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = state.products,
                            key = { it.product.id }
                        ) {
                            ProductItem(
                                product = it.product,
                                orderStatus = it.orderStatus,
                                onOrderClick = { onOrderedItem(it.product.id) }
                            )
                        }
                    }
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}