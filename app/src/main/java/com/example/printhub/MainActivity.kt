package com.example.printhub

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.ViewModelProvider
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val addButton = findViewById<View>(R.id.add)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.SecondFragment -> addButton.visibility = View.GONE
                R.id.FirstFragment -> addButton.visibility = View.VISIBLE
                else -> addButton.visibility = View.VISIBLE
            }
        }

        addButton.setOnClickListener {
            val intent = Intent(this, AddDeviceActivity::class.java)
            startActivity(intent)
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
}