package com.example.printhub

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddDeviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)

        val editSerialNumber = findViewById<EditText>(R.id.edit_serial_number)
        val editName = findViewById<EditText>(R.id.edit_name)
        val editLastServiceDate = findViewById<EditText>(R.id.edit_last_service_date)
        val editStatus = findViewById<EditText>(R.id.edit_status)
        val btnSave = findViewById<Button>(R.id.btn_save_device)

        // Установить текущую дату по умолчанию
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        editLastServiceDate.setText(dateFormat.format(calendar.time))

        // Открыть DatePickerDialog при клике на поле даты
        editLastServiceDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, { _, y, m, d ->
                calendar.set(y, m, d)
                editLastServiceDate.setText(dateFormat.format(calendar.time))
            }, year, month, day).show()
        }

        btnSave.setOnClickListener {
            val serialNumber = editSerialNumber.text.toString().trim()
            if (serialNumber.isEmpty()) {
                Toast.makeText(this, "Серийный номер обязателен", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val name = editName.text.toString().trim()
            val lastServiceDate = editLastServiceDate.text.toString().trim()
            val status = editStatus.text.toString().trim()
            // Пока что сохраняем только локально (например, в памяти или лог)
            val device = Device(
                serialNumber = serialNumber,
                name = name,
                lastServiceDate = lastServiceDate,
                status = status,
                history = emptyList()
            )
            Toast.makeText(this, "Устройство добавлено!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
