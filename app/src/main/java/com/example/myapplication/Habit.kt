package com.example.myapplication

import java.io.Serializable

data class Habit(
    val name: String,
    val description: String,
    val priority: Int,
    val type: HabitType,
    val quantity: Int,
    val periodicity: Int
) : Serializable
