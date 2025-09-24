package com.example.sgroupmobile2025.home

import java.io.Serializable

data class Product(
    val name: String,         // 👈 sửa lại từ Int -> String
    val price: String,        // giữ String để hiển thị "96$"
    val description: String,
    val image: Int,           // ảnh chính
    val gallery: List<Int>,   // danh sách ảnh nhỏ
    val sizes: List<String>   // danh sách size
) : Serializable


