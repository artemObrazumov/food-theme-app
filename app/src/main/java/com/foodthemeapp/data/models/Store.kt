package com.foodthemeapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Store(
    val id: String = "",
    val icon: String = "",
    val name: String = "",
    val address: String = ""
)
