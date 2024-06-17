package com.example.apz_proj_mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apz_proj_mobile.adapter.DeviceAdapter
import com.example.apz_proj_mobile.model.Device
import com.example.apz_proj_mobile.service.HouseService
import com.example.apz_proj_mobile.service.TokenStorageService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class DevicesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private lateinit var tokenStorageService: TokenStorageService
    private lateinit var fabAddDevice: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        recyclerView = findViewById(R.id.recyclerViewDevices)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val houseId = intent.getLongExtra("HOUSE_ID", -1)
        if (houseId != -1L) {
            lifecycleScope.launch {
                val devices = getDevices(houseId)
                if (devices.isNotEmpty()) {
                    deviceAdapter = DeviceAdapter(devices)
                    recyclerView.adapter = deviceAdapter
                } else {
                    Toast.makeText(this@DevicesActivity, "Failed to load devices", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Invalid house ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        fabAddDevice = findViewById(R.id.fabAddDevice)
        fabAddDevice.setOnClickListener {
            val intent = Intent(this, AddDeviceActivity::class.java)
            intent.putExtra("HOUSE_ID", houseId)
            startActivityForResult(intent, REQUEST_CODE_ADD_DEVICE)
        }
    }

    private suspend fun getDevices(houseId: Long): List<Device> {
        tokenStorageService = TokenStorageService(this)
        val token = tokenStorageService.getToken()
        return HouseService().getDevices(token!!, houseId)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_DEVICE && resultCode == Activity.RESULT_OK) {
            recreate()
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_DEVICE = 1
    }
}