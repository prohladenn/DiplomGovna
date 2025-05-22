package com.example.printhub

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.printhub.databinding.ActivityAddDeviceBinding
import java.text.SimpleDateFormat
import java.util.*

class AddDeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установка текущей даты
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        binding.dateText.text = "Дата добавления: $currentDate"

        // Настройка спиннера состояния
        val states = listOf("ИСПРАВЕН", "НЕИСПРАВЕН", "В РЕМОНТЕ", "ДОНОР")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, states)
        binding.stateSpinner.adapter = adapter

        binding.addDeviceBtn.setOnClickListener {
            val serial = binding.serialEdit.text.toString().trim()
            val model = binding.modelEdit.text.toString().trim()
            val state = binding.stateSpinner.selectedItem.toString()
            val date = currentDate
            if (serial.isEmpty() || model.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Возвращаем данные в MainActivity
            val resultIntent = intent
            resultIntent.putExtra("serial", serial)
            resultIntent.putExtra("model", model)
            resultIntent.putExtra("date", date)
            resultIntent.putExtra("state", state)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
