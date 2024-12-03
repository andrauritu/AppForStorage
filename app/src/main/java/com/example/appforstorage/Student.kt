package com.example.appforstorage.com.example.appforstorage
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "mean_grade") val meanGrade: Float
)