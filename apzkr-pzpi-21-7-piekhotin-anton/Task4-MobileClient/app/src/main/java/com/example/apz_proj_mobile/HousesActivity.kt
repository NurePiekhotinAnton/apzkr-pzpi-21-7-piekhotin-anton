package com.example.apz_proj_mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apz_proj_mobile.adapter.HouseAdapter
import com.example.apz_proj_mobile.model.House
import com.example.apz_proj_mobile.service.HouseService
import com.example.apz_proj_mobile.service.TokenStorageService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HousesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var houseAdapter: HouseAdapter
    private lateinit var tokenStorageService: TokenStorageService
    private lateinit var houseService: HouseService
    private lateinit var fabAddHouse: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_houses)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        recyclerView = findViewById(R.id.recyclerView)
        fabAddHouse = findViewById(R.id.fabAddHouse)
        recyclerView.layoutManager = LinearLayoutManager(this)

        tokenStorageService = TokenStorageService(this)
        houseService = HouseService()

        lifecycleScope.launch {
            val userId = tokenStorageService.getId()
            val token = tokenStorageService.getToken()
            val houses = houseService.getHouses(userId, token)
            if (houses != null) {
                houseAdapter = HouseAdapter(houses, { house ->
                    editHouse(house.id, house)
                }, { house ->
                    toDevices(house.id)
                })
                recyclerView.adapter = houseAdapter
            } else {
                Toast.makeText(this@HousesActivity, "Failed to load houses", Toast.LENGTH_SHORT).show()
            }
        }

        fabAddHouse.setOnClickListener {
            val intent = Intent(this, AddHouseActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_HOUSE)
        }
    }

    private fun editHouse(houseId: Long, house: House) {
        Toast.makeText(this, "Editing house with ID: $houseId", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, EditHouseActivity::class.java)
        intent.putExtra("HOUSE_ID", houseId)
        intent.putExtra("HOUSE_NAME", house.name)
        intent.putExtra("HOUSE_ADDRESS", house.address)
        startActivity(intent)
    }

    private fun toDevices(houseId: Long) {
        val intent = Intent(this, DevicesActivity::class.java)
        intent.putExtra("HOUSE_ID", houseId)
        startActivity(intent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_HOUSE && resultCode == Activity.RESULT_OK) {
            recreate()
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_HOUSE = 1
    }
}
