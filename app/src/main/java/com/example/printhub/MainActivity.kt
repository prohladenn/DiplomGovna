package com.example.printhub

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.printhub.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import androidx.activity.viewModels
import com.example.printhub.DeviceViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val deviceViewModel: DeviceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Динамически добавляем заголовок
        val headerContainer = findViewById<FrameLayout>(R.id.header_container)
        val headerView = LayoutInflater.from(this).inflate(R.layout.header_title, headerContainer, false)
        headerContainer.addView(headerView)
        val headerTitle = headerView.findViewById<TextView>(R.id.header_title)

        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> {
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    navController.navigate(R.id.SecondFragment)
                    headerTitle.text = "Личный кабинет"
                    true
                }
                R.id.navigation_list -> {
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    navController.navigate(R.id.FirstFragment)
                    headerTitle.text = "Список устройств"
                    true
                }
                else -> false
            }
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.SecondFragment -> binding.add.hide()
                R.id.FirstFragment -> binding.add.show()
                else -> binding.add.show()
            }
        }

        binding.add.setOnClickListener {
            val intent = Intent(this, AddDeviceActivity::class.java)
            startActivityForResult(intent, 1001)
        }
    }

    override fun onStart() {
        super.onStart()
        // Если пользователь не авторизован — отправляем на AuthActivity
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Меню не используется
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            val serial = data.getStringExtra("serial") ?: return
            val model = data.getStringExtra("model") ?: return
            val date = data.getStringExtra("date") ?: return
            val state = data.getStringExtra("state") ?: return
            val device = Device(serial, model, date, state)
            deviceViewModel.addDevice(device)
        }
    }
}