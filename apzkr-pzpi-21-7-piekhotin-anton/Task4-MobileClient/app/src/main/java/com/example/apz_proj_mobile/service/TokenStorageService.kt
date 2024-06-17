package com.example.apz_proj_mobile.service

import android.content.Context
import android.content.SharedPreferences

/**
 * A service class for managing token storage.
 *
 * @property sharedPreferences SharedPreferences instance used for storing tokens and IDs.
 * @constructor Creates a new instance of TokenStorageService.
 *
 * @param context The context in which the SharedPreferences instance will be created.
 */
class TokenStorageService(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


    //Clears all data from the SharedPreferences instance.
    fun signOut() {
        sharedPreferences.edit().clear().apply()
    }

    // Saves a token to the SharedPreferences instance.
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    // Retrieves a token from the SharedPreferences instance.
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    // Saves an ID to the SharedPreferences instance.
    fun saveId(id: Long) {
        sharedPreferences.edit().putLong(ID_KEY, id).apply()
    }

    // Retrieves an ID from the SharedPreferences instance.
    fun getId(): Long {
        return sharedPreferences.getLong(ID_KEY, 0)
    }

    companion object {
        /**
         * Key used for storing and retrieving tokens.
         */
        private const val TOKEN_KEY = "auth-token"

        /**
         * Key used for storing and retrieving IDs.
         */
        private const val ID_KEY = "auth-id"
    }
}