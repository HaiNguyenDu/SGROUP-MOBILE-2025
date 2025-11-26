package com.example.sgroupmobile2025.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgroupmobile2025.api.RetrofitInstance
import com.example.sgroupmobile2025.common.constants.API_KEY
import com.example.sgroupmobile2025.data.model.AutoCompleteResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class GoongViewModel(application: Application): ViewModel() {
    val goongService = RetrofitInstance.getGoongService()
    private var _isLoad = MutableStateFlow<Boolean>(false)

    private val _responsePlaces = MutableStateFlow<AutoCompleteResponse?>(null)
    val isLoading: StateFlow<Boolean> = _isLoad

    val responsePlaces: MutableStateFlow<AutoCompleteResponse?> = _responsePlaces
    fun getPlaces(input: String, apiKey: String = API_KEY){
        viewModelScope.launch {
            try{
                _isLoad.value = true
                val response = goongService.getPlaces(input, apiKey)
                _isLoad.value = false
                _responsePlaces.value = response
            }catch (e: Exception){
                Log.e("Error", e.toString())
            }

        }
    }
}