package com.example.printhub

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditDeviceActivity : AppCompatActivity() {
    private lateinit var modelText: TextView
    private lateinit var serialText: TextView
    private lateinit var dateEdit: EditText
    private lateinit var stateEdit: EditText
    private lateinit var saveChangesButton: Button
    private lateinit var statusHistoryRecycler: RecyclerView
    private lateinit var statusHistoryAdapter: StatusHistoryAdapter
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
        stateEdit = findViewById(R.id.edit_state)
        saveChangesButton = findViewById(R.id.button_save_changes)
        statusHistoryRecycler = findViewById(R.id.recycler_status_history)

        statusHistoryRecycler.layoutManager = LinearLayoutManager(this)

        deviceId = intent.getStringExtra("deviceId")
        userId = intent.getStringExtra("userId")

        // Устанавливаем текущую дату при каждом открытии экрана
        dateEdit.setText(dateFormat.format(Date()))

        if (deviceId != null && userId != null) {
            db.collection("users").document(userId!!)
                .collection("devices")
                .whereEqualTo("serialNumber", deviceId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val doc = querySnapshot.documents.firstOrNull()
                    if (doc != null && doc.exists()) {
                        val name = doc.getString("name") ?: ""
                        val serialNumber = doc.getString("serialNumber") ?: ""
                        val status = doc.getString("status") ?: ""
                        modelText.text = name
                        serialText.text = serialNumber
                        dateEdit.setText(dateFormat.format(Date())) // всегда текущая дата
                        stateEdit.setText(status)
                        val historyList = (doc.get("history") as? List<Map<String, String>>)?.map { it["description"] ?: "" } ?: emptyList()
                        statusHistoryAdapter = StatusHistoryAdapter(historyList)
                        statusHistoryRecycler.adapter = statusHistoryAdapter
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

        saveChangesButton.setOnClickListener {
            val newDate = dateEdit.text.toString()
            val newState = stateEdit.text.toString()
            if (deviceId != null && userId != null) {
                db.collection("users").document(userId!!)
                    .collection("devices")
                    .whereEqualTo("serialNumber", deviceId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val doc = querySnapshot.documents.firstOrNull()
                        if (doc != null && doc.exists()) {
                            doc.reference.update(
                                mapOf(
                                    "lastServiceDate" to newDate,
                                    "status" to newState
                                )
                            ).addOnSuccessListener { finish() }
                        } else {
                            Toast.makeText(this, "Устройство не найдено", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
