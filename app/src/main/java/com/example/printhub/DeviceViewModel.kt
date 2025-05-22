package com.example.printhub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DeviceViewModel : ViewModel() {
    private val _devices = MutableLiveData<MutableList<Device>>(mutableListOf())
    val devices: LiveData<MutableList<Device>> = _devices

    fun addDevice(device: Device) {
        val list = _devices.value ?: mutableListOf()
        list.add(0, device)
        _devices.value = list
    }

    fun setDevices(newDevices: List<Device>) {
        _devices.value = newDevices.toMutableList()
    }
}
