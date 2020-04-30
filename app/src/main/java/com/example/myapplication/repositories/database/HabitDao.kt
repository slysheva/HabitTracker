package com.example.myapplication.repositories.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun getById(id: String?): LiveData<Habit?>

    @Insert
    fun insert(habit: Habit)

    @Delete
    fun delete(habit: Habit)

    @Update
    fun update(habit: Habit)
}