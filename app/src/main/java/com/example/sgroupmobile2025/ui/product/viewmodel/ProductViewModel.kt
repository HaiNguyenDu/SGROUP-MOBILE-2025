package com.example.sgroupmobile2025.ui.product.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.sgroupmobile2025.data.model.DataProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel(application: Application): ViewModel() {
    private var _price = MutableStateFlow<Double>(0.0)
    val price: StateFlow<Double> = _price

    private var _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name
    fun increasePrice(){
        _price.value += 1
    }
    fun decreasePrice(){
        _price.value -= 1
    }
    fun setPrice(value: Double){
        _price.value = value
    }

}