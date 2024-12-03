package com.example.appforstorage

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appforstorage.com.example.appforstorage.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

            // Sort students alphabetically by name
            val sortedStudents = students.sortedBy { it.name }

            // Convert sorted students to a string
            val studentData = sortedStudents.joinToString(separator = "\n") { it.name }

            // Write to a file in internal storage
            writeToFile("students_list.txt", studentData)

            // Update UI to indicate completion
            runOnUiThread {
                displayTextView.text = "File 'students_list.txt' written to internal storage."
            }
        }
    }

    private fun writeToFile(fileName: String, data: String) {
        openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output.write(data.toByteArray())
        }
    }
}
