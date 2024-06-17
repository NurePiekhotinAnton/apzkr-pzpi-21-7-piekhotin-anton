package com.example.apz_proj_mobile.service

import com.example.apz_proj_mobile.constant.URL
import com.example.apz_proj_mobile.model.EditUserRequest
import com.example.apz_proj_mobile.model.GetUserResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class UserService {

    suspend fun getUserDataFromServer(token: String, id: Long): GetUserResponse? {
        val url = "${URL.BASE_URL}user/$id"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext null
                }
                val responseBody = response.body?.string() ?: return@withContext null
                return@withContext Gson().fromJson(responseBody, GetUserResponse::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

    suspend fun updateUserDataOnServer(token: String, userId: Long, user: EditUserRequest): Boolean {
        val url = "${URL.BASE_URL}auth/edit-user/$userId"
        val requestBody = Gson().toJson(user).toRequestBody()
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