package com.example.sgroupmobile2025.data.repository

import android.util.Log
import com.example.sgroupmobile2025.api.RetrofitInstance
import com.example.sgroupmobile2025.data.model.DetailPlace
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class DetailPlaceRepository {
    private val database = FirebaseDatabase.getInstance()
    private val goongService = RetrofitInstance.getGoongService()
    private val detailPlaceRef = database.getReference("detail_place")
    suspend fun getDetailPlace(placeId: String, apiKey: String): DetailPlace? {
        val snapshot = detailPlaceRef.child(placeId).get().await()
        if (snapshot.exists()) {
            return snapshot.getValue(DetailPlace::class.java)
        }

        val response = goongService.getDetail(placeId, apiKey)
        val detailPlace = response.toDetailPlace()

        detailPlaceRef.child(placeId).setValue(detailPlace)

        return detailPlace
    }

}