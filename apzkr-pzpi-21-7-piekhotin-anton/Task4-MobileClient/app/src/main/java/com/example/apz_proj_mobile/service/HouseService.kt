package com.example.apz_proj_mobile.service

import com.example.apz_proj_mobile.constant.URL
import com.example.apz_proj_mobile.model.Device
import com.example.apz_proj_mobile.model.House
import com.example.apz_proj_mobile.model.HouseInfoDto
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class HouseService {

    suspend fun getHouses(userId: Long, token: String?): List<House>? {
        val url = "${URL.BASE_URL}house/my/$userId"
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }
                val responseBody = response.body?.string() ?: return@withContext null
                Gson().fromJson(responseBody, Array<House>::class.java).toList()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun editHouse(token: String, id: Long, houseInfoDto: HouseInfoDto): House? {
        val url = "${URL.BASE_URL}house/edit/$id"
        val requestBody = Gson().toJson(houseInfoDto)
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toRequestBody())
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext null
                }
                return@withContext Gson().fromJson(response.body?.string(), House::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getDevices(token: String, houseId: Long): List<Device> {
        val url = "${URL.BASE_URL}house/devices/$houseId"
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext emptyList()
                }
                val responseBody = response.body?.string() ?: return@withContext emptyList()
                Gson().fromJson(responseBody, Array<Device>::class.java).toList()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun createHouse (token: String, userId: Long, houseInfoDto: HouseInfoDto): Boolean {
        val url = "${URL.BASE_URL}house/$userId/create"
        val requestBody = Gson().toJson(houseInfoDto).toRequestBody()
        val request: Request = Builder()
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