package com.example.myapplication.repositories.inmemory

import com.example.myapplication.HabitType
import java.io.Serializable
import java.util.*

data class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val priority: Int,
    val type: HabitType,
    val quantity: Int,
    val periodicity: Int,
    val creationDate: Date
) : Serializable
