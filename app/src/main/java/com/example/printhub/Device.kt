package com.example.printhub

data class DeviceHistoryRecord(
    val date: String, // Дата изменения (ISO или timestamp)
    val description: String // Описание изменения
)

data class Device(
    val serialNumber: String, // Серийный номер
    val name: String, // Наименование устройства
    val lastServiceDate: String, // Дата последнего обслуживания (ISO или timestamp)
    val status: String, // Статус устройства
    val history: List<DeviceHistoryRecord> // История изменений
)
