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

class EditHouseActivity : AppCompatActivity() {
    private var houseId: Long = -1
    private var houseName: String? = ""
    private var houseAddress: String? = ""
    private lateinit var editHouseName: EditText
    private lateinit var editHouseAddress: EditText
    private lateinit var editHouseButton: Button
    private lateinit var tokenService: TokenStorageService
    private lateinit var houseService: HouseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_house)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        editHouseName = findViewById(R.id.editHouseName)
        editHouseAddress = findViewById(R.id.editHouseAddress)
        editHouseButton = findViewById(R.id.btnSave)
        tokenService = TokenStorageService(this)
        houseService = HouseService()

        houseId = intent.getLongExtra("HOUSE_ID", -1)
        if (houseId == -1L) {
            // Обробка помилки, якщо ID будинку не було передано
            Toast.makeText(this, "Invalid house ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        houseName = intent.getStringExtra("HOUSE_NAME")
        houseAddress = intent.getStringExtra("HOUSE_ADDRESS")
        editHouseName.setText(houseName)
        editHouseAddress.setText(houseAddress)

        editHouseButton.setOnClickListener {
            editHouse()
        }
    }

    private fun editHouse() {
        val houseInfoDto = HouseInfoDto(
            name = editHouseName.text.toString(),
            address = editHouseAddress.text.toString()
        )
        val token = tokenService.getToken()
        lifecycleScope.launch {
            val response = houseService.editHouse(token!!, houseId, houseInfoDto)
            Toast.makeText(this@EditHouseActivity, "House edited successfully", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this@EditHouseActivity, HousesActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
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
