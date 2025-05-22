package com.example.printhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.printhub.Device

class DeviceAdapter(private val devices: MutableList<Device>) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {
    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serialText: TextView = itemView.findViewById(android.R.id.text1)
        val modelText: TextView = itemView.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.serialText.text = "${device.serial} (${device.state})"
        holder.modelText.text = "${device.model}, добавлен: ${device.date}"
    }

    override fun getItemCount(): Int = devices.size

    fun addDevice(device: Device) {
        devices.add(0, device)
        notifyItemInserted(0)
    }

    fun setDevices(newDevices: List<Device>) {
        devices.clear()
        devices.addAll(newDevices)
        notifyDataSetChanged()
    }
}
