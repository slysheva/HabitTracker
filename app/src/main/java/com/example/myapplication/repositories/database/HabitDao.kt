package com.example.myapplication.repositories.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface   HabitDao {

    @Query("SELECT * FROM habits")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun getById(id: Int): LiveData<Habit?>

    @Insert
    suspend fun insert(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Update
    suspend fun update(habit: Habit)
}