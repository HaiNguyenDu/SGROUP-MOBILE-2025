package com.example.sgroupmobile2025.data.repository

import android.app.Dialog
import android.widget.Toast
import com.example.sgroupmobile2025.api.AutoCompleteResponse
import com.example.sgroupmobile2025.api.IApiService
import com.example.sgroupmobile2025.api.RetrofitInstance
import com.example.sgroupmobile2025.common.constants.API_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressRepository {
    private val retrofit = RetrofitInstance.getInstant().create(
        IApiService::class.java
    )

    fun getAddressByName(input:String, onSuccess:(AutoCompleteResponse)->Unit, onFail:(String)->Unit){
        val call = retrofit.getListAddress(input, API_KEY)
        call.enqueue(object : Callback<AutoCompleteResponse>{
            override fun onResponse(
                call: Call<AutoCompleteResponse?>,
                response: Response<AutoCompleteResponse?>
            ) {
                if(response.isSuccessful)
                {
                    response.body()?.let {
                        onSuccess(it)
                    }
                }
                else {
                    onFail("error")
                }
            }

            override fun onFailure(
                call: Call<AutoCompleteResponse?>,
                t: Throwable
            ) {
                onFail(t.message.toString())
            }
        })
    }
}