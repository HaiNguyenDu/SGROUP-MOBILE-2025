package com.example.sgroupmobile2025.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgroupmobile2025.api.RetrofitInstance
import com.example.sgroupmobile2025.common.constants.API_KEY
import com.example.sgroupmobile2025.data.model.AutoCompleteResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GoongViewModel(application: Application): ViewModel() {
    val goongService = RetrofitInstance.getGoongService()
    private val _responsePlaces = MutableStateFlow<AutoCompleteResponse?>(null)
    val responsePlaces: MutableStateFlow<AutoCompleteResponse?> = _responsePlaces
    fun getPlaces(input: String, apiKey: String = API_KEY){
        viewModelScope.launch {
            _responsePlaces.value = goongService.getPlaces(input, apiKey)
        }
    }
}