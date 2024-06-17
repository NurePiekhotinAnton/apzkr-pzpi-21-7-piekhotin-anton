package com.example.apz_proj_mobile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apz_proj_mobile.model.HouseInfoDto
import com.example.apz_proj_mobile.service.HouseService
import com.example.apz_proj_mobile.service.TokenStorageService
import kotlinx.coroutines.launch

class AddHouseActivity : AppCompatActivity() {

    private lateinit var etHouseName: EditText
    private lateinit var etHouseAddress: EditText
    private lateinit var btnSaveHouse: Button
    private lateinit var houseService: HouseService
    private lateinit var tokenStorageService: TokenStorageService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_house)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        etHouseName = findViewById(R.id.etHouseName)
        etHouseAddress = findViewById(R.id.etHouseAddress)
        btnSaveHouse = findViewById(R.id.btnSaveHouse)
        houseService = HouseService()
        tokenStorageService = TokenStorageService(this)

        btnSaveHouse.setOnClickListener {
            val houseName = etHouseName.text.toString()
            val houseAddress = etHouseAddress.text.toString()

            saveHouse(houseName, houseAddress)
        }
    }

    private fun saveHouse(name: String, address: String) {
        val houseToCreate = HouseInfoDto(name, address)
        lifecycleScope.launch {
            val userId = tokenStorageService.getId()
            val token = tokenStorageService.getToken()
            val response = houseService.createHouse(token!!, userId, houseToCreate)
            if (response) {
                setResult(RESULT_OK)
                finish()
                Toast.makeText(
                    this@AddHouseActivity,
                    "House added successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@AddHouseActivity,
                    "Failed to add house",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, HousesActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
