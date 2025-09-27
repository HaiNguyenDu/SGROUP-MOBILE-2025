package com.example.sgroupmobile2025.home

import java.io.Serializable

data class Product(
    val name: String,
    val price: String,
    val image: Int,
    val shortDescription: String = "",
    val longDescription: String = "",
    val gallery: List<Int> = emptyList(),
    val sizes: List<String> = emptyList(),
    var isFavorite: Boolean = false
) : Serializable


