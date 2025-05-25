package com.example.printhub

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditDeviceActivity : AppCompatActivity() {
    private lateinit var modelText: TextView
    private lateinit var serialText: TextView
    private lateinit var dateEdit: EditText
    private lateinit var stateSpinner: Spinner
    private lateinit var saveButton: Button
    private var deviceId: String? = null
    private var userId: String? = null
    private val db = FirebaseFirestore.getInstance()
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_device)

        modelText = findViewById(R.id.text_model)
        serialText = findViewById(R.id.text_serial)
        dateEdit = findViewById(R.id.edit_date)
        stateSpinner = findViewById(R.id.spinner_state)
        saveButton = findViewById(R.id.button_save)

        val states = resources.getStringArray(R.array.device_states)
        stateSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, states)

        deviceId = intent.getStringExtra("deviceId")
        userId = intent.getStringExtra("userId")

        if (deviceId != null && userId != null) {
            db.collection("users").document(userId!!).collection("devices").document(deviceId!!).get()
                .addOnSuccessListener { doc ->
                    val device = doc.toObject(Device::class.java)
                    if (device != null) {
                        modelText.text = device.model
                        serialText.text = device.serialNumber
                        dateEdit.setText(device.lastServiceDate ?: dateFormat.format(Date()))
                        val stateIndex = states.indexOf(device.state)
                        if (stateIndex >= 0) stateSpinner.setSelection(stateIndex)
                    }
                }
        }

        dateEdit.setOnClickListener {
            val cal = Calendar.getInstance()
            val dialog = DatePickerDialog(this, { _, y, m, d ->
                cal.set(y, m, d)
                dateEdit.setText(dateFormat.format(cal.time))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }

        saveButton.setOnClickListener {
            val newDate = dateEdit.text.toString()
            val newState = stateSpinner.selectedItem.toString()
            if (deviceId != null && userId != null) {
                db.collection("users").document(userId!!).collection("devices").document(deviceId!!)
                    .update(mapOf(
                        "lastServiceDate" to newDate,
                        "state" to newState
                    )).addOnSuccessListener { finish() }
            }
        }
    }
}
