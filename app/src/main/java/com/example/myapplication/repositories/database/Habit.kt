package com.example.myapplication.repositories.database

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.HabitType
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@Entity(tableName="habits")
data class Habit(
    @SerializedName("title")
    val name: String,

    val description: String,

    val priority: Int,

    @SerializedName("type")
    @field:TypeConverters(HabitTypeConverter::class) val type: HabitType,

    @SerializedName("count")
    val quantity: Int,

    @SerializedName("frequency")
    val periodicity: Int,

    @SerializedName("date")
    @field:TypeConverters(DateTypeConverter::class) val creationDate: Long
) : Serializable {
    @SerializedName("uid")
    @PrimaryKey
    @NonNull
    var id: String? = null
}