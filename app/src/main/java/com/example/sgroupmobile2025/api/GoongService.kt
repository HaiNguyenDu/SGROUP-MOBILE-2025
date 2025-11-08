package com.example.sgroupmobile2025.api

import com.example.sgroupmobile2025.data.model.AutoCompleteResponse
import com.example.sgroupmobile2025.data.model.DetailPlaceResponse
import com.example.sgroupmobile2025.data.repository.DetailPlaceRepository
import retrofit2.http.GET
import retrofit2.http.Query

interface GoongService {
    @GET("v2/place/autocomplete")
    suspend fun getPlaces(
        @Query("input") input: String,
        @Query("api_key") apiKey: String
    ): AutoCompleteResponse

    @GET("v2/place/detail")
    suspend fun getDetail(
        @Query("place_id") placeId: String,
        @Query("api_key") apiKey: String
    ): DetailPlaceResponse
}