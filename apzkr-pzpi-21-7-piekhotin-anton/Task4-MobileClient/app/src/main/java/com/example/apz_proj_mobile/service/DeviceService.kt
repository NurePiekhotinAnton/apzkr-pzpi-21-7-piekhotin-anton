package com.example.apz_proj_mobile.service

import com.example.apz_proj_mobile.constant.URL
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class DeviceService {

    suspend fun addDevice(token: String, houseId: Long, type: String): Boolean {
        val url = "${URL.BASE_URL}device/$houseId/create"
        val requestBody = Gson().toJson(type).toRequestBody()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}