package com.example.appforstorage

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE)

        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        setContentView(R.layout.activity_main)

        val themeToggle: ToggleButton = findViewById(R.id.themeToggle)

        themeToggle.isChecked = isDarkMode

        themeToggle.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("isDarkMode", isChecked).apply()

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}
