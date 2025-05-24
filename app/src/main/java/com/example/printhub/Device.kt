package com.example.printhub

data class DeviceHistoryRecord(
    var date: String = "", // Дата изменения (ISO или timestamp)
    var description: String = "" // Описание изменения
)

data class Device(
    var serialNumber: String = "", // Серийный номер
    var name: String = "", // Наименование устройства
    var lastServiceDate: String = "", // Дата последнего обслуживания (ISO или timestamp)
    var status: String = "", // Статус устройства
    var history: List<DeviceHistoryRecord> = emptyList() // История изменений
)
