package com.foodthemeapp.screen.main.store

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.foodthemeapp.StoreScreen
import com.foodthemeapp.components.StoreItem
import com.foodthemeapp.data.models.Store

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.StoreScreen(
    state: StoreScreenState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onStoreClicked: (store: Store) -> Unit
) {
    if (state is StoreScreenState.Content) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(2)
        ) {
            items(
                items = state.stores
            ) {
                StoreItem(
                    store = it,
                    onItemClicked = { onStoreClicked(it) },
                    animatedVisibilityScope = animatedVisibilityScope,
                    sharedTransitionScope = sharedTransitionScope
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

//@Preview
//@Composable
//fun StoreScreenPreview() {
//    StoreScreen(state = StoreScreenState.Content(
//        stores = listOf(
//            Store(id = "1", icon = "", name = "123"),
//            Store(id = "1", icon = "", name = "123"),
//            Store(id = "1", icon = "", name = "123"),
//            Store(id = "1", icon = "", name = "123"),
//            Store(id = "1", icon = "", name = "123"),
//        )
//    ))
//}