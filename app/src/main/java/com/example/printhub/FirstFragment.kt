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
        adapter = DeviceAdapter(devices)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(16))

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            progressBar.visibility = View.VISIBLE
            messageText.visibility = View.GONE
            recyclerView.visibility = View.GONE
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(user.uid)
                .collection("devices")
                .addSnapshotListener { snapshot, error ->
                    progressBar.visibility = View.GONE
                    if (error != null) {
                        messageText.text = "Ошибка загрузки: ${'$'}{error.message}"
                        messageText.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                        return@addSnapshotListener
                    }
                    val loadedDevices = snapshot?.documents?.mapNotNull { it.toObject(Device::class.java) } ?: emptyList()
                    adapter.updateData(loadedDevices)
                    if (loadedDevices.isEmpty()) {
                        messageText.text = "Устройств не найдено"
                        messageText.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        messageText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
        } else {
            messageText.text = "Пользователь не авторизован"
            messageText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }
}