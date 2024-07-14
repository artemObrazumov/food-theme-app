package com.foodthemeapp.screen.main.store

import com.foodthemeapp.data.models.Store

sealed class StoreScreenState {

    data object Idle: StoreScreenState()
    data object Loading: StoreScreenState()
    data class Content(
        val stores: List<Store>
    ): StoreScreenState()
}
