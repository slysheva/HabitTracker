package com.example.myapplication.repositories.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.HabitType
import java.io.Serializable
import java.util.*

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)  var id: Int? = null,
    val name: String,
    val description: String,
    val priority: Int,
    @field:TypeConverters(HabitTypeConverter::class) val type: HabitType,
    val quantity: Int,
    val periodicity: Int,
    @field:TypeConverters(DateTypeConverter::class) val creationDate: Date
) : Serializable
