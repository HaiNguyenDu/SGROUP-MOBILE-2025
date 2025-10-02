package com.example.sgroupmobile2025.data.repository

import com.example.sgroupmobile2025.data.model.DataProduct

object ProductRepository {
    private val _products: MutableList<DataProduct> = mutableListOf()
    val products: List<DataProduct> get() = _products
    fun addProduct(data: DataProduct){
        _products.add(data)
    }
}