package com.example.apz_proj_mobile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apz_proj_mobile.model.EditUserRequest
import com.example.apz_proj_mobile.model.GetUserResponse
import com.example.apz_proj_mobile.service.TokenStorageService
import com.example.apz_proj_mobile.service.UserService
import kotlinx.coroutines.launch


class UserProfileActivity : AppCompatActivity() {

    private lateinit var etUserName: EditText
    private lateinit var etUserEmail: EditText
    private lateinit var btnSaveData: Button
    private lateinit var tokenStorageService: TokenStorageService
    private lateinit var userService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        etUserName = findViewById(R.id.etUserName)
        etUserEmail = findViewById(R.id.etUserEmail)
        btnSaveData = findViewById(R.id.btnSaveData)
        tokenStorageService = TokenStorageService(this)
        userService = UserService()

        lifecycleScope.launch {
            val token = tokenStorageService.getToken()
            val id = tokenStorageService.getId()
            getUserDataFromServer(token!!, id)
        }

        btnSaveData.setOnClickListener {
            lifecycleScope.launch {
                saveData()
            }
        }
    }


    private suspend fun getUserDataFromServer(token: String, userId: Long): GetUserResponse? {
        val user = userService.getUserDataFromServer(token, userId)
        if (user == null) {
            Toast.makeText(
                this@UserProfileActivity,
                "Failed to load user data",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            etUserName.setText(user.name)
            etUserEmail.setText(user.email)
            return user
        }
        return null
    }

    private suspend fun saveData(): Boolean {
        val name = etUserName.text.toString()
        val email = etUserEmail.text.toString()
        val token = tokenStorageService.getToken()!!
        val userId = tokenStorageService.getId()

        val editUserRequest = EditUserRequest(name, email, true, "CLIENT")
        val response = userService.updateUserDataOnServer(token, userId, editUserRequest)
        if (response) {
            Toast.makeText(
                this@UserProfileActivity,
                "User data updated",
                Toast.LENGTH_SHORT
            ).show()
            return true
        } else {
            Toast.makeText(
                this@UserProfileActivity,
                "Failed to update user data",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, HomePageActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
