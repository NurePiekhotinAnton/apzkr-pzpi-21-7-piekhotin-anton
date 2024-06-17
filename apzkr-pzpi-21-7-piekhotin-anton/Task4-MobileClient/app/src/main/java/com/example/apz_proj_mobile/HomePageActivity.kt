package com.example.apz_proj_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.apz_proj_mobile.service.AuthService

class HomePageActivity : AppCompatActivity() {

    private lateinit var btnToHouses: AppCompatButton
    private lateinit var btnToProfile: AppCompatButton
    private lateinit var btnLogout: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)

        btnToHouses = findViewById(R.id.btnToHouses)
        btnToProfile = findViewById(R.id.btnToProfile)
        btnLogout = findViewById(R.id.btnLogout)

        btnToHouses.setOnClickListener {
            val intent = Intent(this@HomePageActivity, HousesActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnToProfile.setOnClickListener {
            val intent = Intent(this@HomePageActivity, UserProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogout.setOnClickListener {
            AuthService.getInstance(this)!!.logout()
            val intent = Intent(this@HomePageActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



}