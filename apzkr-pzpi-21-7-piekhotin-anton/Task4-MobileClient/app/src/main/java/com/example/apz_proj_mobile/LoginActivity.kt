package com.example.apz_proj_mobile

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apz_proj_mobile.service.AuthService
import com.example.apz_proj_mobile.service.LoginRequest
import com.example.apz_proj_mobile.service.RegisterRequest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            if (validInput()) {
                lifecycleScope.launch {
                    loginUser()
                }
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Invalid input! Please check your email and password.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun loginUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        val response = AuthService.getInstance(this)!!
            .login(LoginRequest(email, password))

        if (response != null){
            if (response.message == "Success"){
                // TODO: change to home page
                val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
                Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT)
                    .show()
                startActivity(intent)
                finish()
            }
        }
    }

    private fun validInput(): Boolean {
        val isEmailValid = isEmailValid(etEmail)
        val isPasswordValid = isPasswordValid(etPassword)

        return isEmailValid && isPasswordValid
    }

    companion object {
        fun isEmailValid(emailEditText: EditText?): Boolean {
            val email: CharSequence = emailEditText!!.text.toString().trim { it <= ' ' }
            return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
        }

        fun isPasswordValid(passwordEditText: EditText?): Boolean {
            return passwordEditText!!.text.toString().trim { it <= ' ' }.length >= 6
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

