package com.example.sgroupmobile2025.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgroupmobile2025.common.constants.API_KEY
import com.example.sgroupmobile2025.data.model.DetailPlace
import com.example.sgroupmobile2025.data.repository.DetailPlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailPlaceViewModel(application: Application): ViewModel() {
    private var _detailPlace = MutableStateFlow<DetailPlace?>(null)
    private var _isLoading = MutableStateFlow<Boolean>(false)
    private val detailPlaceRepository = DetailPlaceRepository()
    val isLoading: MutableStateFlow<Boolean> = _isLoading
    val detailPlace: MutableStateFlow<DetailPlace?> = _detailPlace
    fun initDetailPlace(placeId: String){
        viewModelScope.launch {
            _isLoading.value = true;
            _detailPlace.value = detailPlaceRepository.getDetailPlace(placeId, API_KEY)
            _isLoading.value = false
        }
    }
}