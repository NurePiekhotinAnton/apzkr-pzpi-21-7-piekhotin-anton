package com.example.apz_proj_mobile

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apz_proj_mobile.service.AuthService
import com.example.apz_proj_mobile.service.RegisterRequest
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {
    private var registerButton: Button? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        registerButton = findViewById<View>(R.id.btnRegister) as Button
        emailInput = findViewById<View>(R.id.etEmail) as EditText
        passwordInput = findViewById<View>(R.id.etPassword) as EditText

        registerButton!!.setOnClickListener {
            if (validInput()) {
                lifecycleScope.launch {
                    attemptRegister()
                }
            } else {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Invalid input! Please check your email and password.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validInput(): Boolean {
        val isEmailValid = isEmailValid(emailInput)
        val isPasswordValid = isPasswordValid(passwordInput)

        return isEmailValid && isPasswordValid
    }

    private suspend fun attemptRegister() {
        val email = emailInput!!.text.toString()
        val password = passwordInput!!.text.toString()

        val response = AuthService.getInstance(this)!!
            .register(RegisterRequest(email, password))

        if (response != null) {
            if (response.message == "Created") {
                val intent = Intent(
                    this@RegistrationActivity,
                    LoginActivity::class.java
                )
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT)
                    .show()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        }
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