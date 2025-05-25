package com.example.printhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceAdapter(private var devices: List<Device>, private val onItemClick: (Device) -> Unit) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {
    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val modelText: TextView = itemView.findViewById(R.id.device_model)
        val serialText: TextView = itemView.findViewById(R.id.device_serial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.modelText.text = device.name
        holder.serialText.text = device.serialNumber
        holder.itemView.setOnClickListener { onItemClick(device) }
    }

    override fun getItemCount(): Int = devices.size

    fun updateData(newDevices: List<Device>) {
        devices = newDevices
        notifyDataSetChanged()
    }
}
