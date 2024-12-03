package com.example.appforstorage

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appforstorage.com.example.appforstorage.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import android.widget.ToggleButton

import android.provider.MediaStore
import android.content.ContentValues
import android.os.Build
class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
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

        db = AppDatabase.getDatabase(this)

        val displayTextView: TextView = findViewById(R.id.displayTextView)
        CoroutineScope(Dispatchers.IO).launch {
            val students = db.studentDao().getAll()

            val sortedStudents = students.sortedByDescending { it.meanGrade }

            val studentData = sortedStudents.joinToString(separator = "\n") {
                "Name: ${it.name}, MeanGrade: ${it.meanGrade}"
            }

            writeToSharedStorage("students_sorted_by_grade.txt", studentData)

            runOnUiThread {
                displayTextView.text = "File 'students_sorted_by_grade.txt' written to shared storage."
            }
        }
    }

    private fun writeToSharedStorage(fileName: String, data: String) {
        val resolver = contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/")
            }
        }

        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write(data.toByteArray())
                outputStream.flush()
            }
        }
    }
}