package com.example.appforstorage

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appforstorage.com.example.appforstorage.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import android.provider.MediaStore
import android.content.ContentValues
import android.os.Build

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayTextView: TextView = findViewById(R.id.displayTextView)

        // Initialize the database
        db = AppDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            // Fetch students from the database
            val students = db.studentDao().getAll()

            // Sort students by MeanGrade
            val sortedStudents = students.sortedByDescending { it.meanGrade }

            // Convert sorted students to a string
            val studentData = sortedStudents.joinToString(separator = "\n") {
                "Name: ${it.name}, MeanGrade: ${it.meanGrade}"
            }

            // Write to a file in shared storage
            writeToSharedStorage("students_sorted_by_grade.txt", studentData)

            // Update UI to indicate completion
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
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/") // Save in Documents folder
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