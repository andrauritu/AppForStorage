package com.example.appforstorage.com.example.appforstorage
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDao {

    @Insert
    suspend fun insertAll(vararg students: Student)

    @Query("SELECT * FROM student")
    suspend fun getAll(): List<Student>

    @Query("SELECT * FROM student WHERE name LIKE :name")
    suspend fun findByName(name: String): Student?
}