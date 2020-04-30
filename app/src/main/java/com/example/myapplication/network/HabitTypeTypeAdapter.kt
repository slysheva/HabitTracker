package com.example.myapplication.network

import com.example.myapplication.HabitType
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class HabitTypeTypeAdapter: TypeAdapter<HabitType>() {
    override fun write(writer: JsonWriter?, habitType: HabitType?) {
        writer?.value(habitType?.num)
    }

    override fun read(reader: JsonReader?): HabitType {
        return HabitType.fromInt(reader?.nextInt()!!)
    }
}