package com.example.sgroupmobile2025.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgroupmobile2025.api.RetrofitInstance
import com.example.sgroupmobile2025.common.constants.API_KEY
import com.example.sgroupmobile2025.data.model.DetailPlace
import com.example.sgroupmobile2025.data.model.DirectionResponse
import com.example.sgroupmobile2025.data.repository.DetailPlaceRepository
import com.example.sgroupmobile2025.common.constants.MAP_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailPlaceViewModel(application: Application) : ViewModel() {
    private val detailPlaceRepository = DetailPlaceRepository()
    private val goongService = RetrofitInstance.getGoongService()

    private var _detailOriginPlace = MutableStateFlow<DetailPlace?>(null)
    private var _detailDestinationPlace = MutableStateFlow<DetailPlace?>(null)
    private var _direction = MutableStateFlow<DirectionResponse?>(null)
    private var _isLoading = MutableStateFlow<Boolean>(false)

    val isLoading = _isLoading
    val detailOriginPlace = _detailOriginPlace
    val detailDestinationPlace = _detailDestinationPlace
    val direction = _direction

    fun initDetailPlace(originId: String, destinationId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _detailOriginPlace.value = detailPlaceRepository.getDetailPlace(originId, API_KEY)
            _detailDestinationPlace.value = detailPlaceRepository.getDetailPlace(destinationId, API_KEY)

            _isLoading.value = false
        }
    }

    fun getDirection(origin: String, destination: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _direction.value = goongService.getDirection(
                    origin = origin,
                    destination = destination,
                    apiKey = API_KEY
                )
                _isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _isLoading.value = false
            }
        }
    }
}
