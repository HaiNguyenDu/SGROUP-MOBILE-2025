package com.example.sgroupmobile2025

sealed class HomeItem {
    data class Product(val imageRes: Int) : HomeItem()
    data class Text(val brand: String, val description: String, val cost: String) : HomeItem()
}
