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
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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