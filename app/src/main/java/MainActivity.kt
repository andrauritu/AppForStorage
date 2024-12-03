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

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE)

        // Apply the saved theme before setting the content view
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Set the content view using XML layout
        setContentView(R.layout.activity_main)

        // Initialize the ToggleButton (from activity_main.xml)
        val themeToggle: ToggleButton = findViewById(R.id.themeToggle)

        // Set the toggle button state based on saved preference
        themeToggle.isChecked = isDarkMode

        // Set listener for the toggle button
        themeToggle.setOnCheckedChangeListener { _, isChecked ->
            // Save the theme preference
            sharedPreferences.edit().putBoolean("isDarkMode", isChecked).apply()

            // Apply the selected theme
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}
