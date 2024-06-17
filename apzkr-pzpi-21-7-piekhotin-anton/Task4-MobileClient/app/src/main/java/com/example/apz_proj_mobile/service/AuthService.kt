package com.example.apz_proj_mobile.service

import android.content.Context
import android.util.Log
import com.example.apz_proj_mobile.constant.URL
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AuthService private constructor(context: Context) {

    private val tokenStorage: TokenStorageService
    private val client: OkHttpClient
    private val gson: Gson

    init {
        tokenStorage = TokenStorageService(context)
        client = OkHttpClient()
        gson = Gson()
    }

    /**
     * Logs in the user with the provided credentials
     */
    suspend fun login(loginRequest: LoginRequest?): LoginResponse? {
        if (loginRequest == null) {
            throw IllegalArgumentException("Login request cannot be null")
        }
        val gson = Gson()
        val json = gson.toJson(loginRequest)
        val body: RequestBody = json.toRequestBody(JSON)
        val url = URL.BASE_URL.toString() + "auth/sign-in"
        // Create the request
        val request: Request = Builder()
            .url(url)
            .post(body)
            .build()
        return withContext(Dispatchers.IO) {
            try {
                // Send the request
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: "Unknown error"
                    Log.i("Request failed with status: ", "${response.code} - $errorBody")
                }
                val responseBody = response.body!!.string()

                // Parse the response
                val loginResponse = gson.fromJson(
                    responseBody,
                    LoginResponse::class.java
                )
                tokenStorage.saveToken(loginResponse.token)
                tokenStorage.saveId(loginResponse.guestId)

                val responseToReturn = LoginResponse("Success", loginResponse.token, loginResponse.guestId)
                return@withContext responseToReturn
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Registers a new user with the provided credentials
     */
    suspend fun register(registerRequest: RegisterRequest?): RegisterResponse? {
        if (registerRequest == null) {
            throw IllegalArgumentException("Register request cannot be null")
        }
        val gson = Gson()
        val json = gson.toJson(registerRequest)
        val body: RequestBody = json.toRequestBody(JSON)
        val url = URL.BASE_URL.toString() + "auth/sign-up"
        // Create the request
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        return withContext(Dispatchers.IO) {
            try {
                // Send the request
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: "Unknown error"
                    throw IOException("Request failed with status code: ${response.code} - $errorBody")
                }
                // Get the response
                val responseBody = response.body!!.string()
                gson.fromJson(responseBody, RegisterResponse::class.java)
                return@withContext RegisterResponse("Created")
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // Logs out the user
    fun logout() {
        tokenStorage.signOut()
    }

    companion object {
        private var instance: AuthService? = null
        val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
        fun getInstance(context: Context): AuthService? {
            if (instance == null) {
                instance = AuthService(context)
            }
            return instance
        }
    }
}

class LoginRequest(val email: String, val password: String)
class LoginResponse(val message: String, val token: String, val guestId: Long)
class RegisterRequest(val email: String, val password: String)
class RegisterResponse(val message: String)
