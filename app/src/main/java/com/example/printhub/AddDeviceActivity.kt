package com.example.printhub

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

        val deviceId = intent.getStringExtra("deviceId")
        val userId = intent.getStringExtra("userId")

        if (deviceId != null && userId != null) {
            // Загрузка данных устройства из Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(userId).collection("devices").document(deviceId).get()
                .addOnSuccessListener { doc ->
                    val device = doc.toObject(Device::class.java)
                    if (device != null) {
                        editSerialNumber.setText(device.serialNumber)
                        editName.setText(device.name)
                        editLastServiceDate.setText(device.lastServiceDate)
                        editStatus.setText(device.status)
                        // Можно сделать поля серийного номера и имени неактивными для редактирования
                        editSerialNumber.isEnabled = false
                        editName.isEnabled = false
                    }
                }
        } else {
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
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val now = dateFormat.format(Date())
            val historyRecord = DeviceHistoryRecord(
                date = now,
                description = "$now: $status"
            )
            val device = Device(
                serialNumber = serialNumber,
                name = name,
                lastServiceDate = lastServiceDate,
                status = status,
                history = listOf(historyRecord)
            )
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val db = FirebaseFirestore.getInstance()
                db.collection("users")
                    .document(user.uid)
                    .collection("devices")
                    .add(device)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Устройство добавлено", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
