package com.example.sgroupmobile2025.api

import com.example.sgroupmobile2025.common.constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
        private lateinit var retrofit: Retrofit
        fun getInstant(): Retrofit{
            if(!::retrofit.isInitialized){
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
}