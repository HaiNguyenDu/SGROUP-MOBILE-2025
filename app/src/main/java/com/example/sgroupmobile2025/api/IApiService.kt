package com.example.sgroupmobile2025.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiService {
    @GET("v2/place/autocomplete")
    fun getListAddress(
        @Query("input") input: String,
        @Query("api_key") apiKey: String
    ): Call<AutoCompleteResponse>
}