package com.example.printhub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirstFragment : Fragment() {
    private lateinit var adapter: DeviceAdapter
    private val devices = mutableListOf<Device>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.printer_list)
        val progressBar = view.findViewById<View>(R.id.progress_bar)
        val messageText = view.findViewById<TextView>(R.id.message_text)
        val searchEditText = view.findViewById<android.widget.EditText>(R.id.search_edit_text)
        adapter = DeviceAdapter(devices) { device ->
            val intent = android.content.Intent(requireContext(), EditDeviceActivity::class.java)
            intent.putExtra("deviceId", device.serialNumber)
            intent.putExtra("userId", user?.uid)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (user != null) {
            progressBar.visibility = View.VISIBLE
            messageText.visibility = View.GONE
            recyclerView.visibility = View.GONE
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
            db.collection("users")
                .document(user.uid)
                .collection("devices")
                .get()
                .addOnSuccessListener { result ->
                    devices.clear()
                    for (doc in result) {
                        val device = doc.toObject(Device::class.java)
                        devices.add(device)
                    }
                    adapter.updateData(devices)
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    messageText.visibility = View.VISIBLE
                    messageText.text = "Ошибка загрузки устройств"
                }
        } else {
            messageText.visibility = View.VISIBLE
            messageText.text = "Пользователь не авторизован"
        }

        // Поиск по серийному номеру
        searchEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    adapter.updateData(devices)
                } else {
                    val filtered = devices.filter { it.serialNumber.contains(query, ignoreCase = true) }
                    adapter.updateData(filtered)
                }
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }
}