package com.example.printhub

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvDeviceCount = view.findViewById<TextView>(R.id.tvDeviceCount)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .collection("devices")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    tvDeviceCount.text = "Ошибка загрузки"
                    return@addSnapshotListener
                }
                val count = snapshot?.size() ?: 0
                tvDeviceCount.text = "У вас $count устройств"
            }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }
}