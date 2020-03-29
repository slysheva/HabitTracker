package com.example.myapplication

enum class HabitType(val num: Int) {
    BAD(0),
    GOOD(1);

    companion object {
        private val values = values();
        fun fromInt(value: Int) = values.first { it.num == (value % 2) }
    }
}