package com.example.myapplication.repositories.database

import androidx.room.TypeConverter
import com.example.myapplication.HabitType
import java.util.*

class HabitTypeConverter {
    @TypeConverter
    fun typeFromInt(value: Int): HabitType {
        return HabitType.fromInt(value)
    }

    @TypeConverter
    fun typeToInt(type: HabitType): Int {
        return type.num
    }
}