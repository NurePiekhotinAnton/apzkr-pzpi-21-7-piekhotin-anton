package com.example.apz_proj_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apz_proj_mobile.R
import com.example.apz_proj_mobile.model.House

class HouseAdapter(
    private val houses: List<House>,
    private val editListener: (House) -> Unit,
    private val toDevices: (House) -> Unit
) : RecyclerView.Adapter<HouseAdapter.HouseViewHolder>() {

    class HouseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.houseName)
        val addressTextView: TextView = view.findViewById(R.id.houseAddress)
        val editButton: Button = view.findViewById(R.id.btnEdit)
        val toDevicesButton: Button = view.findViewById(R.id.btnToDevices)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_house, parent, false)
        return HouseViewHolder(view)
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        val house = houses[position]
        holder.nameTextView.text = house.name
        holder.addressTextView.text = house.address
        holder.editButton.setOnClickListener { editListener(house) }
        holder.toDevicesButton.setOnClickListener { toDevices(house) }
    }

    override fun getItemCount() = houses.size
}
