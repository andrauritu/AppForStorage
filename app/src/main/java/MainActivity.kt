package com.example.appforstorage

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appforstorage.com.example.appforstorage.AppDatabase
import com.example.appforstorage.com.example.appforstorage.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the TextView where we'll display student data
        val displayTextView: TextView = findViewById(R.id.displayTextView)

        // Initialize the database
        db = AppDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            db.studentDao().insertAll(
                Student(name = "Alice", year = "Sophomore", meanGrade = 8.5f),
                Student(name = "Bob", year = "Junior", meanGrade = 9.2f)
            )

            val students = db.studentDao().getAll()

            runOnUiThread {
                val displayText = students.joinToString("\n") { student ->
                    "Name: ${student.name}, Year: ${student.year}, Mean Grade: ${student.meanGrade}"
                }
                displayTextView.text = displayText
            }
        }
    }
}
