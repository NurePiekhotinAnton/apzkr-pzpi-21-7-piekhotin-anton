package com.example.apz_proj_mobile

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apz_proj_mobile.service.DeviceService
import com.example.apz_proj_mobile.service.TokenStorageService
import kotlinx.coroutines.launch
import java.io.IOException

class AddDeviceActivity : AppCompatActivity() {
    private lateinit var spinnerDeviceTypes: Spinner
    private lateinit var btnAddDevice: Button
    private lateinit var tokenStorageService: TokenStorageService
    private lateinit var deviceService: DeviceService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        spinnerDeviceTypes = findViewById(R.id.spinnerDeviceTypes)
        btnAddDevice = findViewById(R.id.btnAddDevice)
        tokenStorageService = TokenStorageService(this)
        deviceService = DeviceService()

        val deviceTypes = arrayOf(
            "LIGHT",
            "FAN",
            "AC",
            "TV",
            "DOOR",
            "WINDOW",
            "CAMERA",
            "ALARM",
            "SENSOR",
            "OTHER"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deviceTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDeviceTypes.adapter = adapter

        val houseId = intent.getLongExtra("HOUSE_ID", -1)

        btnAddDevice.setOnClickListener {
            val selectedType = spinnerDeviceTypes.selectedItem.toString()
            if (houseId != -1L) {
                lifecycleScope.launch {
                    val success = addDevice(houseId, selectedType)
                    if (success) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddDeviceActivity,
                            "Failed to add device",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private suspend fun addDevice(houseId: Long, type: String): Boolean {
        val token = tokenStorageService.getToken() ?: throw IOException("Token is null")
        val response = deviceService.addDevice(token, houseId, type)

        if (response) {
            Toast.makeText(this, "Device added", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
